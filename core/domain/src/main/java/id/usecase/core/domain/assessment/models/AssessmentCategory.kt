package id.usecase.core.domain.assessment.models

import java.time.LocalDateTime

data class AssessmentCategory(
    val id: String,
    val name: String,
    val percentage: Double,
    val classRoom: ClassRoom?,
    val createdTime: LocalDateTime,
    val lastModifiedTime: LocalDateTime
)
