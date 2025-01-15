package id.usecase.core.domain.assessment.model.analytics

data class CategoryAnalysis(
    val categoryName: String,
    val averageScore: Float,
    val totalAssessments: Int
)
