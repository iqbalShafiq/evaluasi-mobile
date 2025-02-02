package id.usecase.core.domain.assessment.model.assessment.event

data class Event(
    val id: Int,
    val name: String,
    val purpose: String,
    val categoryId: Int,
    val eventDate: Long,
    val createdTime: Long,
    val lastModifiedTime: Long
)