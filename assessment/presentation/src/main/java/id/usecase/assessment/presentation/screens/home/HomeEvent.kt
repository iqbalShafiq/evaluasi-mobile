package id.usecase.assessment.presentation.screens.home

sealed class HomeEvent {
    data class OnError(val error: Throwable): HomeEvent()
}