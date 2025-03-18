package id.usecase.core.domain.assessment.model.assessment.event

data class Event(
    val id: String,
    val name: String,
    val purpose: String,
    val categoryId: String,
    val eventDate: Long,
    val createdTime: Long,
    val lastModifiedTime: Long
)