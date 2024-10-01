package id.usecase.assessment.presentation.screens.class_room.create.students

import androidx.compose.foundation.text.input.TextFieldState

data class AddStudentsState(
    val studentList: List<AddStudentCardState> = listOf(AddStudentCardState()),
    val isLoading: Boolean = false
)
