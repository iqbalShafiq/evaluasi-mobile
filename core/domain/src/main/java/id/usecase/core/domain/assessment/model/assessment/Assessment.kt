package id.usecase.core.domain.assessment.model.assessment

data class Assessment(
    val id: String,
    val studentId: String,
    val eventId: String,
    val score: Double?,
    val createdTime: Long,
    val lastModifiedTime: Long
)