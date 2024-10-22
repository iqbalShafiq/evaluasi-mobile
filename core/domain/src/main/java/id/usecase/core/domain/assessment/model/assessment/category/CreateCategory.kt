package id.usecase.core.domain.assessment.model.assessment.category

data class CreateCategory(
    val name: String,
    val percentage: Double,
    val classRoomId: String
)