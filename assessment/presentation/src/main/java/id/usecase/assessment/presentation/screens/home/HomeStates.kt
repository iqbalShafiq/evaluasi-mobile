package id.usecase.assessment.presentation.screens.home

import id.usecase.assessment.presentation.models.ClassRoomUi

data class HomeStates(
    val isLoading: Boolean = false,
    val classRooms: List<ClassRoomUi> = emptyList()
)