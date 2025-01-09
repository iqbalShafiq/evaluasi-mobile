package id.usecase.assessment.presentation.model

data class AssessmentEventUi(
    val id: Int,
    val name: String,
    val totalAssessment: Int,
    val categoryId: Int,
    val categoryName: String,
    val classId: Int,
    val eventDate: String,
    val createdTime: String,
    val lastModifiedTime: String,
    val isInProgress: Boolean = false,
    val completionProgress: Float = 0f
)
