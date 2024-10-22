package id.usecase.core.domain.assessment.model.classroom

data class ClassRoom(
    val id: Int,
    val name: String,
    val subject: String,
    val startPeriod: Long,
    val endPeriod: Long?,
    val createdTime: Long,
    val lastModifiedTime: Long
)
