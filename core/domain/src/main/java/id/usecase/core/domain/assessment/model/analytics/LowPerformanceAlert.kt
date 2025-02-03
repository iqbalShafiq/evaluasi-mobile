package id.usecase.core.domain.assessment.model.analytics

data class LowPerformanceAlert(
    val studentName: String,
    val averageScore: Float,
    val lastUpdated: Long
)
