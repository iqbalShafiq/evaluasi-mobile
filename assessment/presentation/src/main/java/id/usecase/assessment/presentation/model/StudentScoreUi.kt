package id.usecase.assessment.presentation.model

data class StudentScoreUi(
    val assessmentId: String,
    val studentId: String,
    val studentName: String,
    val comments: String,
    val score: Double,
    val avgScore: Double
)
