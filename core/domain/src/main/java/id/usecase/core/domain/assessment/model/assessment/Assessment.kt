package id.usecase.core.domain.assessment.model.assessment

data class Assessment(
    val id: Int,
    val studentId: Int,
    val eventId: Int,
    val score: Double?,
    val createdTime: Long,
    val lastModifiedTime: Long
)