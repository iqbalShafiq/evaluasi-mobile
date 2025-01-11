package id.usecase.assessment.presentation.screens.home

sealed class HomeAction {
    data object LoadClassRoom : HomeAction()
    data class UpdateTextField(val state: HomeState) : HomeAction()
}