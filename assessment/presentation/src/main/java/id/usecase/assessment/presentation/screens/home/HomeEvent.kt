package id.usecase.assessment.presentation.screens.home

sealed class HomeEvent {
    data class OnErrorOccurred(val error: Throwable): HomeEvent()
}