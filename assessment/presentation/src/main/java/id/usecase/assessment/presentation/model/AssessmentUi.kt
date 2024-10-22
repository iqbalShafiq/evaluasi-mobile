package id.usecase.assessment.presentation.model

data class AssessmentUi(
    val id: Int,
    val name: String,
    val eventName: String,
    val categoryName: String,
    val date: String,
    val lowestScore: Double,
    val averageScore: Double,
    val highestScore: Double,
)
