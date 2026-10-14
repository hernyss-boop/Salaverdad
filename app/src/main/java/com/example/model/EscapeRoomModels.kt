package com.example.model

enum class ScreenState {
    HOME,
    GATEWAY,
    TEACHER_AUTH,
    TEACHER_PANEL,
    STUDENT_LOGIN,
    GAME,
    FINAL_VAULT,
    VICTORY,
    TIME_OUT
}

enum class EnigmaType {
    CLICKBAIT_ANALYSIS,
    URL_INSPECTION,
    IMAGE_ORDERING,
    BOT_DETECTION,
    CRAAP_PAIRING,
    SIFT_CLOZE
}

data class StudentInfo(
    val isTeam: Boolean = false,
    val leadName: String = "",
    val leadLastName: String = "",
    val school: String = "",
    val course: String = "",
    val teamName: String = "",
    val teamMembers: List<String> = emptyList()
) {
    val displayName: String
        get() = if (isTeam) {
            if (teamName.isNotBlank()) teamName else "Equipo de Investigación"
        } else {
            "$leadName $leadLastName".trim().ifBlank { "Agente de Verificación" }
        }
}

data class Hint(
    val level: Int,
    val title: String,
    val text: String,
    val costDescription: String,
    val penalty: Float
)

data class Enigma(
    val id: Int,
    val title: String,
    val subtitle: String,
    val topic: String,
    val storyIntro: String,
    val missionInstruction: String,
    val type: EnigmaType,
    val givesDigit: Boolean,
    val digitValue: String?,
    val digitClueName: String?,
    val hints: List<Hint>,
    val pedagogicalSummary: String
)

data class EnigmaProgress(
    val enigmaId: Int,
    var attempts: Int = 0,
    var unlockedHints: Set<Int> = emptySet(),
    var isSolved: Boolean = false,
    var score: Float = 1.5f,
    var usedSolutionHint: Boolean = false
)

data class InvestigationNote(
    val id: String,
    val title: String,
    val category: String,
    val summary: String,
    val keyPoints: List<String>,
    val practicalTip: String
)

data class TeacherGuideItem(
    val enigmaId: Int,
    val title: String,
    val evaluatedSkill: String,
    val expectedAnswer: String,
    val unlocksDigit: String?,
    val pedagogicalExplanation: String,
    val commonMistake: String
)
