package id.usecase.assessment.presentation.screens.assessment

sealed class AssessmentEvent {
    data class OnErrorOccurred(val errorMessage: String) : AssessmentEvent()
    data object AssessmentHasSaved : AssessmentEvent()
}