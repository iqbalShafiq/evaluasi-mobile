package id.usecase.core.domain.assessment.model.assessment

data class CreateAssessment(
    val studentId: Int,
    val eventId: Int,
    val score: Double?,
)
