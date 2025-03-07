package id.usecase.assessment.presentation.model

data class StudentScoreUi(
    val assessmentId: Int,
    val studentId: Int,
    val studentName: String,
    val comments: String,
    val score: Double,
    val avgScore: Double
)
