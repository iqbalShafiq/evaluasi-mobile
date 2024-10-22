package id.usecase.assessment.presentation.screens.class_room.create

import id.usecase.assessment.presentation.model.ClassRoomUi

sealed class CreateClassRoomEvent {
    data class OnErrorOccurred(val message: String) : CreateClassRoomEvent()
    data class OnClassRoomCreated(val classRoomUi: ClassRoomUi) : CreateClassRoomEvent()
}