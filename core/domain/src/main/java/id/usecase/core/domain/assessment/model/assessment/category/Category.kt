package id.usecase.core.domain.assessment.model.assessment.category

data class Category(
    val id: String,
    val name: String,
    val weight: Double,
    val classRoomId: String,
    val createdTime: Long,
    val lastModifiedTime: Long
)
