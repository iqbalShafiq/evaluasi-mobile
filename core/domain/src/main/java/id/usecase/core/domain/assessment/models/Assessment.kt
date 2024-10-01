package id.usecase.core.domain.assessment.models

import java.time.LocalDateTime

data class Assessment(
    val id: String,
    val student: Student?,
    val event: AssessmentEvent?,
    val score: Double?,
    val createdTime: LocalDateTime,
    val lastModifiedTime: LocalDateTime
)