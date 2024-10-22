package id.usecase.core.domain.assessment.model.classroom

import java.time.LocalDateTime

data class CreateClassRoom(
    val name: String,
    val subject: String,
    val startPeriod: Long,
    val endPeriod: Long?,
)
