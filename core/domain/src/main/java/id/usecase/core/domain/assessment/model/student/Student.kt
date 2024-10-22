package id.usecase.core.domain.assessment.model.student

data class Student(
    val id: Int,
    val classRoomId: Int,
    val name: String,
    val identifier: Int,
    val createdTime: Long,
    val lastModifiedTime: Long
)