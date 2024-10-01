package id.usecase.assessment.presentation.models

data class StudentScoreUi(
    val studentId: String,
    val studentName: String,
    val comments: String,
    val score: Double,
    val avgScore: Double
)
