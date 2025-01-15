package id.usecase.core.domain.assessment.model.analytics

data class StudentProgress(
    val studentName: String,
    val progressPercentage: Float,
    val lastUpdated: Long
)
