package id.usecase.core.domain.assessment.models

import java.time.LocalDateTime

data class Student(
    val id: String,
    val name: String,
    val identifier: Int,
    val createdTime: LocalDateTime,
    val lastModifiedTime: LocalDateTime
)