package id.usecase.core.domain.assessment.model.section

data class Section(
    val id: String,
    val name: String,
    val topics: List<String>,
    val classRoomId: String,
    val createdTime: Long,
    val lastModifiedTime: Long
)
