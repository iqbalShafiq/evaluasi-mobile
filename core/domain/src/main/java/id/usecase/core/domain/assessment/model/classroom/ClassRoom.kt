package id.usecase.core.domain.assessment.model.classroom

data class ClassRoom(
    val id: String,
    val name: String,
    val subject: String,
    val description: String,
    val startPeriod: Long,
    val longPeriod: Long?,
    val createdTime: Long,
    val lastModifiedTime: Long,
    val schedule: List<Int> = emptyList()
)
