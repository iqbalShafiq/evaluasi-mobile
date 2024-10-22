package id.usecase.assessment.presentation.model

data class AssessmentEventUi(
    val id: Int,
    val name: String,
    val assessedStudentCount: Int,
    val categoryId: Int,
    val categoryName: String,
    val classId: Int,
    val eventDate: String,
    val createdTime: String,
    val lastModifiedTime: String
)
