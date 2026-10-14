package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EscapeRoomRepository
import com.example.model.Enigma
import com.example.model.EnigmaProgress
import com.example.model.EnigmaType
import com.example.ui.components.EscapeHud
import com.example.ui.components.HintDialog
import com.example.ui.components.ResearchFolderDialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBright
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldDigit
import com.example.ui.theme.RoseError
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GameScreen(
    currentEnigma: Enigma,
    totalEnigmas: Int,
    progress: EnigmaProgress?,
    timeRemainingSeconds: Int,
    discoveredDigits: Map<Int, String>,
    score: Float,
    antiCheatAlertMessage: String?,
    isResearchFolderOpen: Boolean,
    activeHintDialogEnigmaId: Int?,
    currentFeedback: String?,
    isSuccess: Boolean,
    onDismissAlert: () -> Unit,
    onOpenResearchFolder: () -> Unit,
    onCloseResearchFolder: () -> Unit,
    onOpenHints: () -> Unit,
    onCloseHints: () -> Unit,
    onUnlockHint: (Int, Int) -> Unit,
    onSuccessSolution: (Int, String) -> Unit,
    onFailedAttempt: (Int, String) -> Unit,
    onAdvanceToNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Persistent HUD
            EscapeHud(
                timeRemainingSeconds = timeRemainingSeconds,
                currentEnigmaId = currentEnigma.id,
                totalEnigmas = totalEnigmas,
                discoveredDigits = discoveredDigits,
                score = score,
                antiCheatAlertMessage = antiCheatAlertMessage,
                onDismissAlert = onDismissAlert,
                onOpenResearchFolder = onOpenResearchFolder,
                onOpenHints = onOpenHints
            )

            // Scrollable Enigma Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Enigma Header & Story
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Slate900),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentEnigma.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CyanBright
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Slate800
                                ) {
                                    Text(
                                        text = currentEnigma.subtitle,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberGlow,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = currentEnigma.storyIntro,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Slate850,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = CyanAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = currentEnigma.missionInstruction,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CyanAccent
                                    )
                                }
                            }
                        }
                    }
                }

                // Interactive Minigame Section
                item {
                    when (currentEnigma.type) {
                        EnigmaType.CLICKBAIT_ANALYSIS -> {
                            EnigmaClickbaitGame(
                                isSolved = progress?.isSolved == true,
                                onVerify = { headlines, emotion ->
                                    val correctHeadlines = setOf(0, 3) // 1 and 4
                                    val isHeadlinesCorrect = headlines == correctHeadlines
                                    val isEmotionCorrect = emotion.equals("Miedo", ignoreCase = true)

                                    if (isHeadlinesCorrect && isEmotionCorrect) {
                                        onSuccessSolution(
                                            currentEnigma.id,
                                            "¡EXCELENTE DEDUCCIÓN! Has aislado los titulares manipuladores. La apelación al miedo buscaba inhibir el juicio crítico. Obtienes el Dígito 1."
                                        )
                                    } else {
                                        val hint = if (!isHeadlinesCorrect) {
                                            "Los titulares fraudulentos usan mayúsculas alarmistas y prometen catástrofes sin citar fuentes oficiales."
                                        } else {
                                            "Revisa la emoción primaria: ¿buscan informar o generar pánico/urgencia?"
                                        }
                                        onFailedAttempt(currentEnigma.id, "❌ Intento fallido (-0.25 pts). $hint")
                                    }
                                }
                            )
                        }

                        EnigmaType.URL_INSPECTION -> {
                            EnigmaUrlInspectionGame(
                                isSolved = progress?.isSolved == true,
                                onVerify = { selectedUrlIndex, keyword ->
                                    val isUrlCorrect = selectedUrlIndex == 2 // 3rd option
                                    val isKeywordCorrect = keyword.trim().equals("CERTIFICADO", ignoreCase = true)

                                    if (isUrlCorrect && isKeywordCorrect) {
                                        onSuccessSolution(
                                            currentEnigma.id,
                                            "¡CORTAFUEGOS RESTAURADO! Detectaste el subdominio engañoso y la extensión .xyz sin HTTPS. Módulo superado con éxito."
                                        )
                                    } else {
                                        val hint = if (!isUrlCorrect) {
                                            "Mira la terminación del dominio: los sitios oficiales de Argentina terminan en .gob.ar o .edu.ar, jamás en .xyz."
                                        } else {
                                            "La palabra clave de seguridad indicada es CERTIFICADO."
                                        }
                                        onFailedAttempt(currentEnigma.id, "❌ Intento fallido (-0.25 pts). $hint")
                                    }
                                }
                            )
                        }

                        EnigmaType.IMAGE_ORDERING -> {
                            EnigmaImageOrderingGame(
                                isSolved = progress?.isSolved == true,
                                onVerify = { order ->
                                    val correctOrder = listOf(1, 2, 3, 4)
                                    if (order == correctOrder) {
                                        onSuccessSolution(
                                            currentEnigma.id,
                                            "¡METODOLOGÍA CORRECTA! Ordenaste la búsqueda inversa cronológicamente y demostraste que la foto era reciclada de 2018. Obtienes el Dígito 2."
                                        )
                                    } else {
                                        onFailedAttempt(
                                            currentEnigma.id,
                                            "❌ Secuencia errónea (-0.25 pts). Recuerda: primero aíslas el archivo, luego usas Google Lens, después buscas la fecha más antigua y finalmente contrastas."
                                        )
                                    }
                                }
                            )
                        }

                        EnigmaType.BOT_DETECTION -> {
                            EnigmaBotDetectionGame(
                                isSolved = progress?.isSolved == true,
                                onVerify = { selectedIndexes ->
                                    val correct = setOf(0, 1, 3) // A, B, D
                                    if (selectedIndexes == correct) {
                                        onSuccessSolution(
                                            currentEnigma.id,
                                            "¡GRANJA DE BOTS DESMANTELADA! Identificaste los 3 factores de automatización y astroturfing. Obtienes el Dígito 3."
                                        )
                                    } else {
                                        onFailedAttempt(
                                            currentEnigma.id,
                                            "❌ Intento fallido (-0.25 pts). Debes seleccionar exactamente las 3 señales técnicas de automatización (cuenta recién nacida con miles de tuits, usuario con números y avatar IA, y mensajes clonados)."
                                        )
                                    }
                                }
                            )
                        }

                        EnigmaType.CRAAP_PAIRING -> {
                            EnigmaCraapGame(
                                isSolved = progress?.isSolved == true,
                                onVerify = { answers ->
                                    // 0->0, 1->1, 2->2, 3->3
                                    val isAllCorrect = answers[0] == 0 && answers[1] == 1 && answers[2] == 2 && answers[3] == 3
                                    if (isAllCorrect) {
                                        onSuccessSolution(
                                            currentEnigma.id,
                                            "¡MATRIZ CRAAP CALIBRADA! Sincronizaste los 4 pilares de evaluación crítica de fuentes. Módulo superado con éxito."
                                        )
                                    } else {
                                        onFailedAttempt(
                                            currentEnigma.id,
                                            "❌ Emparejamiento incorrecto (-0.25 pts). Revisa la correspondencia de Actualidad (fecha), Autoridad (autor), Exactitud (datos) y Propósito (intención)."
                                        )
                                    }
                                }
                            )
                        }

                        EnigmaType.SIFT_CLOZE -> {
                            EnigmaSiftGame(
                                isSolved = progress?.isSolved == true,
                                onVerify = { answers ->
                                    val isCorrect = answers[0].equals("DETENERSE", ignoreCase = true) &&
                                            answers[1].equals("FUENTE", ignoreCase = true) &&
                                            answers[2].equals("MEJOR", ignoreCase = true) &&
                                            answers[3].equals("ORIGINAL", ignoreCase = true)

                                    if (isCorrect) {
                                        onSuccessSolution(
                                            currentEnigma.id,
                                            "¡NÚCLEO FINAL HACKEADO! Has completado el protocolo SIFT de lectura lateral. Obtienes el Dígito 4. ¡Dirígete a la Bóveda Final!"
                                        )
                                    } else {
                                        onFailedAttempt(
                                            currentEnigma.id,
                                            "❌ Términos erróneos (-0.25 pts). SIFT: Detenerse, Investigar la fuente, Buscar mejor cobertura, Rastrear afirmación original."
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                // Feedback & Progression Banner
                if (currentFeedback != null) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSuccess) EmeraldSuccess.copy(alpha = 0.15f) else RoseError.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSuccess) EmeraldSuccess else RoseError
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (isSuccess) EmeraldSuccess else RoseError,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isSuccess) "¡ENIGMA RESUELTO!" else "REVISIÓN REQUERIDA",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSuccess) EmeraldSuccess else RoseError
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = currentFeedback,
                                    fontSize = 13.sp,
                                    color = TextPrimary,
                                    lineHeight = 17.sp
                                )

                                if (isSuccess && currentEnigma.givesDigit && currentEnigma.digitValue != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = GoldDigit.copy(alpha = 0.2f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldDigit),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Key,
                                                contentDescription = null,
                                                tint = GoldDigit,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = "¡DÍGITO DE BÓVEDA OBTENIDO: ${currentEnigma.digitValue}!",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Black,
                                                    fontFamily = FontFamily.Monospace,
                                                    color = GoldDigit
                                                )
                                                Text(
                                                    text = "Se ha almacenado en tu HUD superior para la combinación final.",
                                                    fontSize = 11.sp,
                                                    color = TextPrimary
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "💡 Fundamento Pedagógico: ${currentEnigma.pedagogicalSummary}",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )

                                if (isSuccess) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Button(
                                        onClick = onAdvanceToNext,
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (currentEnigma.id == totalEnigmas) AmberAccent else CyanAccent,
                                            contentColor = Slate950
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("advance_next_enigma_btn")
                                    ) {
                                        Text(
                                            text = if (currentEnigma.id == totalEnigmas) "IR A LA BÓVEDA FINAL" else "AVANZAR AL SIGUIENTE ENIGMA",
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal dialogs
    if (isResearchFolderOpen) {
        ResearchFolderDialog(onDismiss = onCloseResearchFolder)
    }

    if (activeHintDialogEnigmaId != null) {
        val enigmaForHint = EscapeRoomRepository.enigmas.firstOrNull { it.id == activeHintDialogEnigmaId }
        if (enigmaForHint != null) {
            HintDialog(
                enigma = enigmaForHint,
                progress = progress,
                onUnlockHint = { hintLevel -> onUnlockHint(enigmaForHint.id, hintLevel) },
                onDismiss = onCloseHints
            )
        }
    }
}

// -------------------------------------------------------------
// Interactive Minigame 1: Clickbait Analysis
// -------------------------------------------------------------
@Composable
fun EnigmaClickbaitGame(
    isSolved: Boolean,
    onVerify: (Set<Int>, String) -> Unit
) {
    val headlines = listOf(
        "1. ¡URGENTE: Suspenderán las vacaciones de invierno en toda la provincia!",
        "2. El Ministerio de Educación evalúa nuevo calendario escolar para el ciclo lectivo.",
        "3. La directiva del colegio informó el cronograma de exámenes finales de diciembre.",
        "4. ¡ESCÁNDALO: El oscuro plan que nadie quiere que sepas sobre los comedores escolares!"
    )

    val selectedHeadlines = remember { mutableStateListOf<Int>() }
    var selectedEmotion by remember { mutableStateOf("") }
    val emotions = listOf("Miedo", "Tranquilidad", "Aburrimiento", "Alegría")

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Paso 1: Marca los 2 titulares que son CLICKBAIT ENGAÑOSO:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
            Spacer(modifier = Modifier.height(8.dp))

            headlines.forEachIndexed { index, text ->
                val isSelected = selectedHeadlines.contains(index)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) CyanAccent.copy(alpha = 0.15f) else Slate850,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) CyanAccent else Slate700
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isSolved) {
                            if (isSelected) selectedHeadlines.remove(index)
                            else if (selectedHeadlines.size < 2) selectedHeadlines.add(index)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                if (isSelected) selectedHeadlines.remove(index)
                                else if (selectedHeadlines.size < 2) selectedHeadlines.add(index)
                            },
                            enabled = !isSolved,
                            colors = CheckboxDefaults.colors(
                                checkedColor = CyanAccent,
                                checkmarkColor = Slate950
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = text,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Paso 2: ¿Qué emoción primaria intentan manipular estos titulares?",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AmberAccent
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                emotions.forEach { emotion ->
                    FilterChip(
                        selected = selectedEmotion == emotion,
                        onClick = { if (!isSolved) selectedEmotion = emotion },
                        label = { Text(emotion, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmberAccent,
                            selectedLabelColor = Slate950
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onVerify(selectedHeadlines.toSet(), selectedEmotion) },
                enabled = !isSolved && selectedHeadlines.size == 2 && selectedEmotion.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Slate950),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("VERIFICAR TITULARES", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Minigame 2: URL Inspection
// -------------------------------------------------------------
@Composable
fun EnigmaUrlInspectionGame(
    isSolved: Boolean,
    onVerify: (Int, String) -> Unit
) {
    val urls = listOf(
        "A. https://www.argentina.gob.ar/educacion",
        "B. https://abc.gob.ar/portal/comunicados-oficiales",
        "C. http://www.min-educacion.gob.ar.portal-alumnos.xyz/comunicado",
        "D. https://buenosaires.gob.ar/educacion/agenda"
    )

    var selectedUrl by remember { mutableStateOf(-1) }
    var securityWord by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Paso 1: Elige la URL FALSA (Spoofing / Dominio Engañoso):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
            Spacer(modifier = Modifier.height(8.dp))

            urls.forEachIndexed { index, urlText ->
                val isSelected = selectedUrl == index
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) RoseError.copy(alpha = 0.15f) else Slate850,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) RoseError else Slate700
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isSolved) { selectedUrl = index }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { if (!isSolved) selectedUrl = index },
                            colors = RadioButtonDefaults.colors(selectedColor = RoseError)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = urlText,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Paso 2: Escribe la palabra de seguridad que garantiza el cifrado seguro (Pista: 'CERTIFICADO'):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AmberAccent
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = securityWord,
                onValueChange = { securityWord = it.uppercase() },
                placeholder = { Text("Escribe CERTIFICADO") },
                singleLine = true,
                enabled = !isSolved,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmberAccent,
                    unfocusedBorderColor = Slate700,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onVerify(selectedUrl, securityWord) },
                enabled = !isSolved && selectedUrl != -1 && securityWord.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Slate950),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ANALIZAR ENLACE Y CERTIFICADO", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Minigame 3: Image Search Ordering
// -------------------------------------------------------------
@Composable
fun EnigmaImageOrderingGame(
    isSolved: Boolean,
    onVerify: (List<Int>) -> Unit
) {
    val initialItems = remember {
        mutableStateListOf(
            Pair(3, "Revisar fecha y sitio de la publicación más antigua en los resultados"),
            Pair(1, "Descargar la foto sospechosa o copiar la dirección de enlace de la imagen"),
            Pair(4, "Confirmar que la foto era de un siniestro de 2018 y contrastar contexto"),
            Pair(2, "Subir el archivo a Google Lens o TinEye para rastreo visual inverso")
        )
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Acomoda los 4 pasos del protocolo en orden secuencial (Usa las flechas para mover):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
            Spacer(modifier = Modifier.height(10.dp))

            initialItems.forEachIndexed { index, item ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Slate850,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CyanAccent.copy(alpha = 0.2f),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}°",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = item.second,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )

                        if (!isSolved) {
                            Column {
                                if (index > 0) {
                                    IconButton(
                                        onClick = {
                                            val temp = initialItems[index]
                                            initialItems[index] = initialItems[index - 1]
                                            initialItems[index - 1] = temp
                                        },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "Subir", tint = CyanAccent, modifier = Modifier.size(16.dp))
                                    }
                                }
                                if (index < initialItems.size - 1) {
                                    IconButton(
                                        onClick = {
                                            val temp = initialItems[index]
                                            initialItems[index] = initialItems[index + 1]
                                            initialItems[index + 1] = temp
                                        },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "Bajar", tint = CyanAccent, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onVerify(initialItems.map { it.first }) },
                enabled = !isSolved,
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Slate950),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("VERIFICAR PROTOCOLO SECUENCIAL", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Minigame 4: Bot Detection
// -------------------------------------------------------------
@Composable
fun EnigmaBotDetectionGame(
    isSolved: Boolean,
    onVerify: (Set<Int>) -> Unit
) {
    val anomalies = listOf(
        "A. Cuenta creada hace sólo 3 días pero ya tiene más de 3.500 tuits publicados.",
        "B. Nombre de usuario alfanumérico generado al azar (@estudiante_739201) y foto de stock/IA.",
        "C. Tiene fotos etiquetadas con compañeros de aula del colegio y profesores.",
        "D. Envía exactamente el mismo mensaje y hashtags 10 veces por minuto en bucle incesante."
    )

    val selectedAnomalies = remember { mutableStateListOf<Int>() }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Marca exactamente las 3 señales que evidencian que el perfil es un BOT automatizado:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
            Spacer(modifier = Modifier.height(10.dp))

            anomalies.forEachIndexed { index, text ->
                val isSelected = selectedAnomalies.contains(index)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) AmberAccent.copy(alpha = 0.15f) else Slate850,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) AmberAccent else Slate700
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isSolved) {
                            if (isSelected) selectedAnomalies.remove(index)
                            else if (selectedAnomalies.size < 3) selectedAnomalies.add(index)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                if (isSelected) selectedAnomalies.remove(index)
                                else if (selectedAnomalies.size < 3) selectedAnomalies.add(index)
                            },
                            enabled = !isSolved,
                            colors = CheckboxDefaults.colors(
                                checkedColor = AmberAccent,
                                checkmarkColor = Slate950
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = text,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onVerify(selectedAnomalies.toSet()) },
                enabled = !isSolved && selectedAnomalies.size == 3,
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Slate950),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("FILTRAR BOT Y ASTROTURFING", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Minigame 5: CRAAP Matching
// -------------------------------------------------------------
@Composable
fun EnigmaCraapGame(
    isSolved: Boolean,
    onVerify: (Map<Int, Int>) -> Unit
) {
    val criteria = listOf("1. Actualidad", "2. Autoridad", "3. Exactitud", "4. Propósito")
    val questionOptions = listOf(
        "¿Cuándo se publicó y si la fecha sigue vigente?",
        "¿Quién es el autor y cuáles son sus credenciales?",
        "¿Incluye fuentes comprobables y datos contrastados?",
        "¿Busca informar objetivamente o manipular/vender?"
    )

    val assignments = remember { mutableStateMapOf(0 to 0, 1 to 2, 2 to 1, 3 to 3) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Asocia cada criterio CRAAP con su pregunta de verificación correspondiente:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
            Spacer(modifier = Modifier.height(10.dp))

            criteria.forEachIndexed { criterionIndex, criterionName ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = criterionName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberGlow
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Simple picker using chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        questionOptions.forEachIndexed { qIndex, qText ->
                            val isSelected = assignments[criterionIndex] == qIndex
                            FilterChip(
                                selected = isSelected,
                                onClick = { if (!isSolved) assignments[criterionIndex] = qIndex },
                                label = { Text("Opción ${qIndex + 1}", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanAccent,
                                    selectedLabelColor = Slate950
                                )
                            )
                        }
                    }
                    Text(
                        text = "Seleccionada: ${questionOptions[assignments[criterionIndex] ?: 0]}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onVerify(assignments.toMap()) },
                enabled = !isSolved,
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Slate950),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SINCRONIZAR CRITERIOS CRAAP", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Minigame 6: SIFT Cloze
// -------------------------------------------------------------
@Composable
fun EnigmaSiftGame(
    isSolved: Boolean,
    onVerify: (List<String>) -> Unit
) {
    var word1 by remember { mutableStateOf("") }
    var word2 by remember { mutableStateOf("") }
    var word3 by remember { mutableStateOf("") }
    var word4 by remember { mutableStateOf("") }

    val options1 = listOf("DETENERSE", "ACELERAR", "IGNORAR")
    val options2 = listOf("FUENTE", "RUMOR", "BOCETO")
    val options3 = listOf("MEJOR", "PEOR", "MENOR")
    val options4 = listOf("ORIGINAL", "MODIFICADA", "SEGUNDA")

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Completa los 4 pilares de la verificación lateral SIFT:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Blank 1: S
            ClozePickerRow(
                label = "S: Antes de compartir, debes...",
                options = options1,
                selected = word1,
                isSolved = isSolved,
                onSelect = { word1 = it }
            )

            // Blank 2: I
            ClozePickerRow(
                label = "I: Investigar la reputación de la...",
                options = options2,
                selected = word2,
                isSolved = isSolved,
                onSelect = { word2 = it }
            )

            // Blank 3: F
            ClozePickerRow(
                label = "F: Buscar en otros medios una...",
                options = options3,
                selected = word3,
                isSolved = isSolved,
                onSelect = { word3 = it }
            )

            // Blank 4: T
            ClozePickerRow(
                label = "T: Rastrear la afirmación...",
                options = options4,
                selected = word4,
                isSolved = isSolved,
                onSelect = { word4 = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onVerify(listOf(word1, word2, word3, word4)) },
                enabled = !isSolved && word1.isNotBlank() && word2.isNotBlank() && word3.isNotBlank() && word4.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Slate950),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("EJECUTAR PROTOCOLO SIFT", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ClozePickerRow(
    label: String,
    options: List<String>,
    selected: String,
    isSolved: Boolean,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = selected == option,
                    onClick = { if (!isSolved) onSelect(option) },
                    label = { Text(option, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanAccent,
                        selectedLabelColor = Slate950
                    )
                )
            }
        }
    }
}
