package id.usecase.core.domain.assessment.models

import java.time.LocalDateTime

data class ClassRoom(
    val id: String,
    val name: String,
    val subject: String,
    val students: List<Student>,
    val startPeriod: LocalDateTime,
    val endPeriod: LocalDateTime?,
    val createdTime: LocalDateTime,
    val lastModifiedTime: LocalDateTime
)
