package id.usecase.assessment.presentation.screens.class_room.detail

sealed class ClassRoomAction {
    data class LoadClassRoom(val classRoomId: Int) : ClassRoomAction()
}