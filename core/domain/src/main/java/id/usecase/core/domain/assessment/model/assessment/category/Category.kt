package id.usecase.core.domain.assessment.model.assessment.category

data class Category(
    val id: Int,
    val name: String,
    val percentage: Double,
    val classRoomId: Int,
    val createdTime: Long,
    val lastModifiedTime: Long
)
