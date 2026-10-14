package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.EscapeRoomRepository
import com.example.model.EnigmaProgress
import com.example.model.ScreenState
import com.example.model.StudentInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EscapeRoomUiState(
    val currentScreen: ScreenState = ScreenState.HOME,
    val studentInfo: StudentInfo = StudentInfo(),
    val roomPasswordEntered: String = "",
    val roomPasswordError: String? = null,
    val currentRoomCode: String = EscapeRoomRepository.DEFAULT_ROOM_PASSWORD,
    val teacherCodeEntered: String = "",
    val teacherCodeError: String? = null,
    val timeRemainingSeconds: Int = EscapeRoomRepository.TOTAL_TIME_SECONDS,
    val isTimerRunning: Boolean = false,
    val currentEnigmaIndex: Int = 0,
    val enigmaProgressMap: Map<Int, EnigmaProgress> = emptyMap(),
    val discoveredDigits: Map<Int, String> = emptyMap(), // enigmaId -> digit
    val isResearchFolderOpen: Boolean = false,
    val activeHintDialogEnigmaId: Int? = null,
    val antiCheatWarningsCount: Int = 0,
    val antiCheatAlertMessage: String? = null,
    val finalVaultInput: String = "",
    val finalVaultAttempts: Int = 0,
    val finalVaultError: String? = null,
    val currentEnigmaFeedback: String? = null,
    val isCurrentEnigmaSuccess: Boolean = false
)

class EscapeRoomViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EscapeRoomUiState())
    val uiState: StateFlow<EscapeRoomUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        initializeProgress()
    }

    private fun initializeProgress() {
        val initialMap = EscapeRoomRepository.enigmas.associate { enigma ->
            enigma.id to EnigmaProgress(enigmaId = enigma.id)
        }
        _uiState.update { it.copy(enigmaProgressMap = initialMap) }
    }

    fun navigateTo(screen: ScreenState) {
        _uiState.update { it.copy(currentScreen = screen, roomPasswordError = null, teacherCodeError = null) }
    }

    fun updateStudentInfo(info: StudentInfo) {
        _uiState.update { it.copy(studentInfo = info) }
    }

    fun updateRoomPasswordInput(input: String) {
        _uiState.update { it.copy(roomPasswordEntered = input, roomPasswordError = null) }
    }

    fun validateRoomPassword(): Boolean {
        val entered = _uiState.value.roomPasswordEntered.trim().uppercase()
        val expected = _uiState.value.currentRoomCode.trim().uppercase()
        return if (entered == expected) {
            _uiState.update { it.copy(currentScreen = ScreenState.STUDENT_LOGIN, roomPasswordError = null) }
            true
        } else {
            _uiState.update { it.copy(roomPasswordError = "Clave de sala incorrecta. Solicítala a tu docente.") }
            false
        }
    }

    fun updateTeacherCodeInput(input: String) {
        _uiState.update { it.copy(teacherCodeEntered = input, teacherCodeError = null) }
    }

    fun validateTeacherCode(): Boolean {
        val entered = _uiState.value.teacherCodeEntered.trim()
        return if (entered == EscapeRoomRepository.TEACHER_SECRET_KEY) {
            _uiState.update { it.copy(currentScreen = ScreenState.TEACHER_PANEL, teacherCodeError = null) }
            true
        } else {
            _uiState.update { it.copy(teacherCodeError = "Contraseña de docente incorrecta.") }
            false
        }
    }

    fun updateCustomRoomCode(newCode: String) {
        if (newCode.isNotBlank()) {
            _uiState.update { it.copy(currentRoomCode = newCode.trim().uppercase()) }
        }
    }

    fun startMission() {
        _uiState.update {
            it.copy(
                currentScreen = ScreenState.GAME,
                currentEnigmaIndex = 0,
                timeRemainingSeconds = EscapeRoomRepository.TOTAL_TIME_SECONDS,
                isTimerRunning = true,
                discoveredDigits = emptyMap(),
                antiCheatWarningsCount = 0,
                antiCheatAlertMessage = null,
                finalVaultInput = "",
                finalVaultAttempts = 0,
                finalVaultError = null,
                currentEnigmaFeedback = null,
                isCurrentEnigmaSuccess = false
            )
        }
        initializeProgress()
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val currentSec = _uiState.value.timeRemainingSeconds
                if (currentSec <= 1) {
                    _uiState.update {
                        it.copy(
                            timeRemainingSeconds = 0,
                            isTimerRunning = false,
                            currentScreen = ScreenState.TIME_OUT
                        )
                    }
                    break
                } else {
                    _uiState.update { it.copy(timeRemainingSeconds = currentSec - 1) }
                }
            }
        }
    }

    fun onAppPaused() {
        // Anti-cheat detection when user minimizes or leaves the escape room during active game
        if (_uiState.value.isTimerRunning && (_uiState.value.currentScreen == ScreenState.GAME || _uiState.value.currentScreen == ScreenState.FINAL_VAULT)) {
            val penalizedTime = (_uiState.value.timeRemainingSeconds - 60).coerceAtLeast(10)
            val count = _uiState.value.antiCheatWarningsCount + 1
            _uiState.update {
                it.copy(
                    antiCheatWarningsCount = count,
                    timeRemainingSeconds = penalizedTime,
                    antiCheatAlertMessage = "⚠️ ¡ALERTA ANTI-COPIA! Se detectó salida de la aplicación. Se penaliza con -1:00 minuto."
                )
            }
        }
    }

    fun dismissAntiCheatAlert() {
        _uiState.update { it.copy(antiCheatAlertMessage = null) }
    }

    fun toggleResearchFolder(open: Boolean? = null) {
        _uiState.update {
            it.copy(isResearchFolderOpen = open ?: !it.isResearchFolderOpen)
        }
    }

    fun openHintDialog(enigmaId: Int) {
        _uiState.update { it.copy(activeHintDialogEnigmaId = enigmaId) }
    }

    fun closeHintDialog() {
        _uiState.update { it.copy(activeHintDialogEnigmaId = null) }
    }

    fun unlockHint(enigmaId: Int, hintLevel: Int) {
        val currentProgress = _uiState.value.enigmaProgressMap[enigmaId] ?: return
        if (currentProgress.unlockedHints.contains(hintLevel)) return

        val newUnlocked = currentProgress.unlockedHints + hintLevel
        var newScore = currentProgress.score

        if (hintLevel == 2) {
            newScore = (newScore - 0.25f).coerceAtLeast(0f)
        } else if (hintLevel == 3) {
            // Solution given: puzzle gives 0 score
            newScore = 0.0f
        }

        val updatedProgress = currentProgress.copy(
            unlockedHints = newUnlocked,
            score = newScore,
            usedSolutionHint = if (hintLevel == 3) true else currentProgress.usedSolutionHint
        )

        val updatedMap = _uiState.value.enigmaProgressMap.toMutableMap()
        updatedMap[enigmaId] = updatedProgress

        _uiState.update { it.copy(enigmaProgressMap = updatedMap) }
    }

    fun recordFailedAttempt(enigmaId: Int, feedbackMessage: String) {
        val currentProgress = _uiState.value.enigmaProgressMap[enigmaId] ?: return
        val newAttempts = currentProgress.attempts + 1
        val newScore = if (currentProgress.usedSolutionHint) 0f else (currentProgress.score - 0.25f).coerceAtLeast(0f)

        val updatedProgress = currentProgress.copy(
            attempts = newAttempts,
            score = newScore
        )
        val updatedMap = _uiState.value.enigmaProgressMap.toMutableMap()
        updatedMap[enigmaId] = updatedProgress

        _uiState.update {
            it.copy(
                enigmaProgressMap = updatedMap,
                currentEnigmaFeedback = feedbackMessage,
                isCurrentEnigmaSuccess = false
            )
        }
    }

    fun recordSuccessfulSolution(enigmaId: Int, feedbackMessage: String) {
        val enigma = EscapeRoomRepository.enigmas.firstOrNull { it.id == enigmaId } ?: return
        val currentProgress = _uiState.value.enigmaProgressMap[enigmaId] ?: return

        val updatedProgress = currentProgress.copy(
            isSolved = true
        )
        val updatedMap = _uiState.value.enigmaProgressMap.toMutableMap()
        updatedMap[enigmaId] = updatedProgress

        val updatedDigits = _uiState.value.discoveredDigits.toMutableMap()
        if (enigma.givesDigit && enigma.digitValue != null) {
            updatedDigits[enigmaId] = enigma.digitValue
        }

        _uiState.update {
            it.copy(
                enigmaProgressMap = updatedMap,
                discoveredDigits = updatedDigits,
                currentEnigmaFeedback = feedbackMessage,
                isCurrentEnigmaSuccess = true
            )
        }
    }

    fun advanceToNextEnigma() {
        val nextIndex = _uiState.value.currentEnigmaIndex + 1
        if (nextIndex < EscapeRoomRepository.enigmas.size) {
            _uiState.update {
                it.copy(
                    currentEnigmaIndex = nextIndex,
                    currentEnigmaFeedback = null,
                    isCurrentEnigmaSuccess = false
                )
            }
        } else {
            // All 6 enigmas completed -> Go to final vault screen!
            _uiState.update {
                it.copy(
                    currentScreen = ScreenState.FINAL_VAULT,
                    currentEnigmaFeedback = null,
                    isCurrentEnigmaSuccess = false
                )
            }
        }
    }

    fun updateVaultInput(digit: String) {
        val current = _uiState.value.finalVaultInput
        if (current.length < 4) {
            _uiState.update { it.copy(finalVaultInput = current + digit, finalVaultError = null) }
        }
    }

    fun deleteVaultInput() {
        val current = _uiState.value.finalVaultInput
        if (current.isNotEmpty()) {
            _uiState.update { it.copy(finalVaultInput = current.dropLast(1), finalVaultError = null) }
        }
    }

    fun clearVaultInput() {
        _uiState.update { it.copy(finalVaultInput = "", finalVaultError = null) }
    }

    fun submitFinalVaultCode() {
        val entered = _uiState.value.finalVaultInput.trim()
        if (entered == EscapeRoomRepository.FINAL_VAULT_CODE) {
            timerJob?.cancel()
            _uiState.update {
                it.copy(
                    currentScreen = ScreenState.VICTORY,
                    isTimerRunning = false,
                    finalVaultError = null
                )
            }
        } else {
            val newAttempts = _uiState.value.finalVaultAttempts + 1
            // Penalize 2 minutes (-120 seconds) for incorrect vault order!
            val penalizedTime = (_uiState.value.timeRemainingSeconds - 120).coerceAtLeast(10)
            _uiState.update {
                it.copy(
                    finalVaultAttempts = newAttempts,
                    timeRemainingSeconds = penalizedTime,
                    finalVaultInput = "",
                    finalVaultError = "❌ ¡Secuencia incorrecta! Alarma activada: Se han descontado 2:00 minutos del cronómetro."
                )
            }
        }
    }

    fun restartGame() {
        timerJob?.cancel()
        _uiState.update {
            EscapeRoomUiState(
                currentScreen = ScreenState.HOME,
                currentRoomCode = it.currentRoomCode
            )
        }
        initializeProgress()
    }

    fun calculateTotalScore(): Float {
        val enigmaPoints = _uiState.value.enigmaProgressMap.values.sumOf { it.score.toDouble() }.toFloat()
        // Bonus 1.0 point for solving the final cipher with fewer than 3 attempts
        val vaultBonus = if (_uiState.value.finalVaultAttempts == 0) 1.0f else (1.0f - (_uiState.value.finalVaultAttempts * 0.25f)).coerceAtLeast(0.25f)
        val antiCheatDeduction = _uiState.value.antiCheatWarningsCount * 0.5f
        return (enigmaPoints + vaultBonus - antiCheatDeduction).coerceIn(0f, 10f)
    }

    fun getPerformanceReport(): PerformanceReport {
        val totalScore = calculateTotalScore()
        val totalAttempts = _uiState.value.enigmaProgressMap.values.sumOf { it.attempts } + _uiState.value.finalVaultAttempts
        val totalHints = _uiState.value.enigmaProgressMap.values.sumOf { it.unlockedHints.size }
        val timeSpentSeconds = EscapeRoomRepository.TOTAL_TIME_SECONDS - _uiState.value.timeRemainingSeconds

        val topicsToReinforce = mutableListOf<String>()
        _uiState.value.enigmaProgressMap.forEach { (enigmaId, progress) ->
            val enigma = EscapeRoomRepository.enigmas.firstOrNull { it.id == enigmaId } ?: return@forEach
            if (progress.score < 1.0f || progress.attempts > 1 || progress.usedSolutionHint) {
                topicsToReinforce.add("${enigma.title}: ${enigma.topic}")
            }
        }

        val performanceGrade = when {
            totalScore >= 9.0f -> "🏆 EXPERTO EN FACT-CHECKING (Sobresaliente)"
            totalScore >= 7.0f -> "⭐ VERIFICADOR DESTACADO (Muy Bueno)"
            totalScore >= 6.0f -> "✔️ AGENTE APROBADO (Bueno)"
            else -> "⚠️ EN PROCESO DE APRENDIZAJE (Requiere Refuerzo)"
        }

        return PerformanceReport(
            studentInfo = _uiState.value.studentInfo,
            finalScore = totalScore,
            grade = performanceGrade,
            timeSpentFormatted = formatSeconds(timeSpentSeconds),
            timeRemainingFormatted = formatSeconds(_uiState.value.timeRemainingSeconds),
            totalErrors = totalAttempts,
            totalHintsUsed = totalHints,
            antiCheatIncidents = _uiState.value.antiCheatWarningsCount,
            topicsToReinforce = topicsToReinforce
        )
    }

    private fun formatSeconds(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

data class PerformanceReport(
    val studentInfo: StudentInfo,
    val finalScore: Float,
    val grade: String,
    val timeSpentFormatted: String,
    val timeRemainingFormatted: String,
    val totalErrors: Int,
    val totalHintsUsed: Int,
    val antiCheatIncidents: Int,
    val topicsToReinforce: List<String>
)
