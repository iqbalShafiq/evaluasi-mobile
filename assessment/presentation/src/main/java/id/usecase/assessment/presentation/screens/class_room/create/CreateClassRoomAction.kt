package id.usecase.assessment.presentation.screens.class_room.create

sealed class CreateClassRoomAction {
    data class LoadClassRoomDetail(val classRoomId: Int) : CreateClassRoomAction()
    data object CreateClassRoom : CreateClassRoomAction()
    data class SetStartDate(val startDate: String) : CreateClassRoomAction()
}