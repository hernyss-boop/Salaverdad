package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.EscapeRoomRepository
import com.example.model.ScreenState
import com.example.ui.screens.FinalVaultScreen
import com.example.ui.screens.GameScreen
import com.example.ui.screens.GatewayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StudentLoginScreen
import com.example.ui.screens.TeacherPanelScreen
import com.example.ui.screens.TimeOutScreen
import com.example.ui.screens.VictoryScreen
import com.example.ui.theme.OperacionVerdadTheme
import com.example.ui.theme.Slate950
import com.example.viewmodel.EscapeRoomViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: EscapeRoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OperacionVerdadTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Slate950
                ) {
                    EscapeRoomApp(viewModel = viewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Anti-cheat detection when user minimizes or exits the app during the escape room
        viewModel.onAppPaused()
    }
}

@Composable
fun EscapeRoomApp(viewModel: EscapeRoomViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle back button according to current screen
    when (uiState.currentScreen) {
        ScreenState.HOME -> {
            // System default exit
        }
        ScreenState.GATEWAY -> {
            BackHandler { viewModel.navigateTo(ScreenState.HOME) }
        }
        ScreenState.TEACHER_AUTH, ScreenState.TEACHER_PANEL -> {
            BackHandler { viewModel.navigateTo(ScreenState.GATEWAY) }
        }
        ScreenState.STUDENT_LOGIN -> {
            BackHandler { viewModel.navigateTo(ScreenState.GATEWAY) }
        }
        ScreenState.GAME -> {
            // Prevent accidental back exit during escape room
            BackHandler {
                viewModel.toggleResearchFolder(true)
            }
        }
        ScreenState.FINAL_VAULT -> {
            BackHandler {
                // Keep inside vault challenge
            }
        }
        ScreenState.VICTORY, ScreenState.TIME_OUT -> {
            BackHandler { viewModel.restartGame() }
        }
    }

    when (uiState.currentScreen) {
        ScreenState.HOME -> {
            HomeScreen(
                onEnterClicked = { viewModel.navigateTo(ScreenState.GATEWAY) }
            )
        }

        ScreenState.GATEWAY, ScreenState.TEACHER_AUTH -> {
            GatewayScreen(
                roomPasswordInput = uiState.roomPasswordEntered,
                roomPasswordError = uiState.roomPasswordError,
                onRoomPasswordChange = { viewModel.updateRoomPasswordInput(it) },
                onValidateRoomPassword = { viewModel.validateRoomPassword() },
                teacherPasswordInput = uiState.teacherCodeEntered,
                teacherPasswordError = uiState.teacherCodeError,
                onTeacherPasswordChange = { viewModel.updateTeacherCodeInput(it) },
                onValidateTeacherPassword = { viewModel.validateTeacherCode() },
                onBackToHome = { viewModel.navigateTo(ScreenState.HOME) }
            )
        }

        ScreenState.TEACHER_PANEL -> {
            TeacherPanelScreen(
                currentRoomCode = uiState.currentRoomCode,
                onUpdateRoomCode = { viewModel.updateCustomRoomCode(it) },
                onBack = { viewModel.navigateTo(ScreenState.GATEWAY) }
            )
        }

        ScreenState.STUDENT_LOGIN -> {
            StudentLoginScreen(
                onStartGame = { studentInfo ->
                    viewModel.updateStudentInfo(studentInfo)
                    viewModel.startMission()
                },
                onBack = { viewModel.navigateTo(ScreenState.GATEWAY) }
            )
        }

        ScreenState.GAME -> {
            val currentEnigma = EscapeRoomRepository.enigmas[uiState.currentEnigmaIndex]
            val currentProgress = uiState.enigmaProgressMap[currentEnigma.id]

            GameScreen(
                currentEnigma = currentEnigma,
                totalEnigmas = EscapeRoomRepository.enigmas.size,
                progress = currentProgress,
                timeRemainingSeconds = uiState.timeRemainingSeconds,
                discoveredDigits = uiState.discoveredDigits,
                score = viewModel.calculateTotalScore(),
                antiCheatAlertMessage = uiState.antiCheatAlertMessage,
                isResearchFolderOpen = uiState.isResearchFolderOpen,
                activeHintDialogEnigmaId = uiState.activeHintDialogEnigmaId,
                currentFeedback = uiState.currentEnigmaFeedback,
                isSuccess = uiState.isCurrentEnigmaSuccess,
                onDismissAlert = { viewModel.dismissAntiCheatAlert() },
                onOpenResearchFolder = { viewModel.toggleResearchFolder(true) },
                onCloseResearchFolder = { viewModel.toggleResearchFolder(false) },
                onOpenHints = { viewModel.openHintDialog(currentEnigma.id) },
                onCloseHints = { viewModel.closeHintDialog() },
                onUnlockHint = { enigmaId, hintLevel -> viewModel.unlockHint(enigmaId, hintLevel) },
                onSuccessSolution = { enigmaId, message -> viewModel.recordSuccessfulSolution(enigmaId, message) },
                onFailedAttempt = { enigmaId, message -> viewModel.recordFailedAttempt(enigmaId, message) },
                onAdvanceToNext = { viewModel.advanceToNextEnigma() }
            )
        }

        ScreenState.FINAL_VAULT -> {
            FinalVaultScreen(
                vaultInput = uiState.finalVaultInput,
                attempts = uiState.finalVaultAttempts,
                error = uiState.finalVaultError,
                timeRemainingSeconds = uiState.timeRemainingSeconds,
                discoveredDigits = uiState.discoveredDigits,
                score = viewModel.calculateTotalScore(),
                antiCheatAlertMessage = uiState.antiCheatAlertMessage,
                onDigitClick = { digit -> viewModel.updateVaultInput(digit) },
                onDeleteClick = { viewModel.deleteVaultInput() },
                onSubmit = { viewModel.submitFinalVaultCode() },
                onDismissAlert = { viewModel.dismissAntiCheatAlert() },
                onOpenResearchFolder = { viewModel.toggleResearchFolder(true) }
            )
        }

        ScreenState.VICTORY -> {
            VictoryScreen(
                report = viewModel.getPerformanceReport(),
                onRestart = { viewModel.restartGame() }
            )
        }

        ScreenState.TIME_OUT -> {
            TimeOutScreen(
                report = viewModel.getPerformanceReport(),
                onRestart = { viewModel.restartGame() }
            )
        }
    }
}
