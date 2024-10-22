package id.usecase.assessment.presentation.model

data class ClassRoomUi(
    val id: Int,
    val className: String,
    val subject: String,
    val lastAssessment: String,
    val studentCount: Int,
    val startPeriod: String,
    val endPeriod: String
)