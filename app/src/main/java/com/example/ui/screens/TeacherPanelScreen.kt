package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EscapeRoomRepository
import com.example.model.TeacherGuideItem
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.CyanAccent
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
fun TeacherPanelScreen(
    currentRoomCode: String,
    onUpdateRoomCode: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var isEditingCode by remember { mutableStateOf(false) }
    var newCodeText by remember { mutableStateOf(currentRoomCode) }

    val tabs = listOf("Respuestas & Solucionario", "Guía Didáctica", "Versión Papel / Física")

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copiado", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("teacher_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Panel de Control Docente",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                    Text(
                        text = "Administración, Solucionario y Pedagogía",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // Room Credentials Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberAccent.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CLAVE DE SALA PARA ESTUDIANTES:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                            if (isEditingCode) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    OutlinedTextField(
                                        value = newCodeText,
                                        onValueChange = { newCodeText = it.uppercase() },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = CyanAccent,
                                            unfocusedBorderColor = Slate700,
                                            focusedTextColor = TextPrimary,
                                            unfocusedTextColor = TextPrimary
                                        ),
                                        modifier = Modifier.width(140.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            onUpdateRoomCode(newCodeText)
                                            isEditingCode = false
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Slate950)
                                    ) {
                                        Text("Guardar", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            } else {
                                Text(
                                    text = currentRoomCode,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyanAccent
                                )
                            }
                        }

                        if (!isEditingCode) {
                            Row {
                                IconButton(onClick = { isEditingCode = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar clave",
                                        tint = TextSecondary
                                    )
                                }
                                IconButton(onClick = { copyToClipboard("Clave de Sala", currentRoomCode) }) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copiar clave",
                                        tint = CyanAccent
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Slate850,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = GoldDigit,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Código Bóveda Final: 7392 (E1: 7, E3: 3, E4: 9, E6: 2)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldDigit
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Navigation Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Slate900,
                contentColor = CyanAccent,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = AmberAccent
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) AmberGlow else TextSecondary
                            )
                        }
                    )
                }
            }

            // Tab Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        // Solucionario de los 6 enigmas
                        items(EscapeRoomRepository.teacherGuideItems) { item ->
                            TeacherEnigmaAnswerCard(item)
                        }
                    }
                    1 -> {
                        // Guía Didáctica y cómo presentar la sala
                        item {
                            PedagogicalPresentationGuide()
                        }
                    }
                    2 -> {
                        // Adaptación física / papel
                        item {
                            PhysicalClassroomGuide()
                        }
                    }
                }
            }

            // Bottom return button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Slate900)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onBack,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Slate800,
                        contentColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text(
                        text = "Volver a la Pantalla de Alumnos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TeacherEnigmaAnswerCard(item: TeacherGuideItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent
                )
                if (item.unlocksDigit != null && item.unlocksDigit != "Ninguno") {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = GoldDigit.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Dígito: ${item.unlocksDigit}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldDigit,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Habilidad evaluada: ${item.evaluatedSkill}",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Slate850,
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "SOLUCIÓN CORRECTA:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.expectedAnswer,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Explicación pedagógica: ${item.pedagogicalExplanation}",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "⚠️ Error frecuente: ${item.commonMistake}",
                fontSize = 11.sp,
                color = AmberAccent
            )
        }
    }
}

@Composable
fun PedagogicalPresentationGuide() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "1. Cómo presentar la sala a los estudiantes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberGlow
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Narrativa introductoria: Plantear el escape room como una misión urgente del periódico escolar. Explicar que la red DeepEcho hackeó la base de datos y que sólo el pensamiento crítico y el rigor informativo pueden detenerla.\n" +
                            "• Dinámica de equipos: Se sugiere jugar en parejas o tríos de 3 o 4 alumnos. Asignar roles dentro del equipo:\n" +
                            "   - Líder Verificador: coordina el debate de respuestas.\n" +
                            "   - Detective de Fuentes: consulta los apuntes de la Carpeta de Investigación.\n" +
                            "   - Cronometrador: vigila los 30 minutos y los fallos.\n" +
                            "• Regla de oro: No adivinar al azar; cada intento fallido resta 0.25 puntos.",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "2. Gestión de pistas y evaluación",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Pista 1 (Gratis): Guía pedagógica sin costo. Animar a los alumnos a leerla si se sienten desorientados.\n" +
                            "• Pista 2 (-0.25 pts): Pista focalizada para evitar frustración.\n" +
                            "• Pista 3 (Solución): Pensada para grupos bloqueados; da la respuesta para avanzar pero califica con 0 ese enigma.\n" +
                            "• Al terminar, los alumnos mostrarán su pantalla de resumen con la nota final (sobre 10), errores y temas a reforzar.",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun PhysicalClassroomGuide() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Print, contentDescription = null, tint = EmeraldSuccess)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guía para Adaptar en Papel / Físico / Presencial",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Si prefieres realizar la actividad en el aula sin dispositivos, puedes implementarla de la siguiente manera:\n\n" +
                            "1. Estaciones de Trabajo (6 Mesas):\n" +
                            "Imprime cada uno de los 6 enigmas en una cartulina o ficha. En cada mesa coloca los materiales de apoyo (fichas de la Carpeta de Investigación).\n\n" +
                            "2. Sobres de Pistas Cerrados:\n" +
                            "En cada mesa coloca 3 sobres cerrados rotulados como 'Pista 1 (Gratis)', 'Pista 2 (Penalización -0.25)' y 'Pista 3 (Solución)'. Los estudiantes deben anotar en su hoja de ruta si abrieron algún sobre.\n\n" +
                            "3. Bóveda Final con Candado de 4 Dígitos:\n" +
                            "Consigue una caja o cofre escolar con un candado numérico de combinación. Configura la combinación en: 7 - 3 - 9 - 2.\n" +
                            "Dentro del cofre coloca los certificados de 'Agentes Oficiales de Verificación' o golosinas/insignias de premio.\n\n" +
                            "4. Hoja de Control del Docente:\n" +
                            "El docente cronometra 30 minutos en la pizarra y sella la hoja de cada equipo cuando completan cada enigma.",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
