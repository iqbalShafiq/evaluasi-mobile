package id.usecase.core.domain.assessment.model.assessment.event

data class CreateEvent(
    val name: String,
    val categoryId: Int,
    val eventDate: Long,
)
