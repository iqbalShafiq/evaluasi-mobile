package id.usecase.core.domain.assessment.model.student

data class Student(
    val id: String,
    val classRoomId: String,
    val name: String,
    val identifier: Int,
    val createdTime: Long,
    val lastModifiedTime: Long
)