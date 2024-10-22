package id.usecase.assessment.presentation.screens.class_room.create

sealed class CreateClassRoomAction {
    data object CreateClassRoom : CreateClassRoomAction()
}