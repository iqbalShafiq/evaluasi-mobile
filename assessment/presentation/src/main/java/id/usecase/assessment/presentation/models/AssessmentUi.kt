package id.usecase.assessment.presentation.models

data class AssessmentUi(
    val id: String,
    val name: String,
    val eventName: String,
    val categoryName: String,
    val date: String,
    val lowestScore: Double,
    val averageScore: Double,
    val highestScore: Double,
)
