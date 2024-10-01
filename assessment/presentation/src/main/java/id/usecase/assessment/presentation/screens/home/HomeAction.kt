package id.usecase.assessment.presentation.screens.home

sealed class HomeAction {
    data object LoadClassRoom : HomeAction()
    data class OpenClassRoom(val classRoomId: String) : HomeAction()
    data object AddClassRoom: HomeAction()
}