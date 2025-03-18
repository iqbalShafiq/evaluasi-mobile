package id.usecase.assessment.presentation.screens.class_room.create

sealed class CreateClassRoomAction {
    data class LoadClassRoomDetail(val classRoomId: String) : CreateClassRoomAction()
    data object CreateClassRoom : CreateClassRoomAction()
    data class UpdateTextField(val state: CreateClassRoomState) : CreateClassRoomAction()
}