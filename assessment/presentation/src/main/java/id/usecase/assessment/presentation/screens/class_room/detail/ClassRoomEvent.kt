package id.usecase.assessment.presentation.screens.class_room.detail

sealed class ClassRoomEvent {
    data class OnErrorOccurred(val message: String) : ClassRoomEvent()
}