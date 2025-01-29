package id.usecase.core.domain.assessment.model.section

data class Section(
    val id: Int = 0,
    val name: String,
    val topics: List<String>,
    val classRoomId: Int,
    val createdTime: Long,
    val lastModifiedTime: Long
)
