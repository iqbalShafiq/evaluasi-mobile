package id.usecase.assessment.presentation.screens.home

import id.usecase.assessment.presentation.model.ClassRoomUi

data class HomeState(
    val isLoading: Boolean = false,
    val querySearch: String = "",
    val classRooms: List<ClassRoomUi> = emptyList(),
    val searchResult: List<ClassRoomUi> = emptyList(),
    val totalStudent: Int = 0
)