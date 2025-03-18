package id.usecase.assessment.presentation.model

data class AssessmentEventUi(
    val id: String,
    val name: String,
    val totalAssessment: Int,
    val categoryId: String,
    val categoryName: String,
    val classId: String,
    val eventDate: String,
    val createdTime: String,
    val lastModifiedTime: String,
    val isInProgress: Boolean = false,
    val completionProgress: Float = 0f
)
