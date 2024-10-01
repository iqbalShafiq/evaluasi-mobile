package id.usecase.assessment.presentation.models

data class AlertUi(
    val studentName: String,
    val studentIdentifier: Int,
    val studentAverageScore: Double,
    val classAverageScore: Double,
    val detectedDate: String,
    val alert: String
)