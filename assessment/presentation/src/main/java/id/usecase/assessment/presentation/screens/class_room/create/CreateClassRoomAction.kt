package id.usecase.assessment.presentation.screens.class_room.create

import androidx.compose.ui.text.input.TextFieldValue

sealed class CreateClassRoomAction {
    data class LoadClassRoomDetail(val classRoomId: Int) : CreateClassRoomAction()
    data object CreateClassRoom : CreateClassRoomAction()
    data class SetClassRoomName(val text: TextFieldValue) : CreateClassRoomAction()
    data class SetSubject(val text: TextFieldValue) : CreateClassRoomAction()
    data class SetEndDate(val text: TextFieldValue) : CreateClassRoomAction()
    data class SetStartDate(val text: TextFieldValue) : CreateClassRoomAction()
    data class SetDescription(val text: TextFieldValue) : CreateClassRoomAction()
}