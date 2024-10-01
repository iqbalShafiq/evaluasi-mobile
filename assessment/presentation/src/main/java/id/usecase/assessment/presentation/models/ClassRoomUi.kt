package id.usecase.assessment.presentation.models

data class ClassRoomUi(
    val id: String,
    val className: String,
    val subject: String,
    val lastAssessment: String,
    val studentCount: Int,
    val startPeriod: String,
    val endPeriod: String
)