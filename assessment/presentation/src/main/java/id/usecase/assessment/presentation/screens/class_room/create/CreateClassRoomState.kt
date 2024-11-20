package id.usecase.assessment.presentation.screens.class_room.create

import androidx.compose.foundation.text.input.TextFieldState
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentItemState
import id.usecase.core.domain.assessment.model.classroom.ClassRoom

data class CreateClassRoomState(
    val isLoading: Boolean = false,
    val classRoom: ClassRoom? = null,
    val classRoomName: TextFieldState = TextFieldState(),
    val subject: TextFieldState = TextFieldState(),
    val startDate: TextFieldState = TextFieldState(),
    val endDate: TextFieldState = TextFieldState(),
    val students: List<AddStudentItemState> = listOf(),
)
