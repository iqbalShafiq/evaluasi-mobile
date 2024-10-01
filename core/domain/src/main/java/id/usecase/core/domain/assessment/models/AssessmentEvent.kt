package id.usecase.core.domain.assessment.models

import java.time.LocalDateTime

data class AssessmentEvent(
    val id: String,
    val name: String,
    val category: AssessmentCategory?,
    val eventDate: LocalDateTime,
    val createdTime: LocalDateTime,
    val lastModifiedTime: LocalDateTime
)