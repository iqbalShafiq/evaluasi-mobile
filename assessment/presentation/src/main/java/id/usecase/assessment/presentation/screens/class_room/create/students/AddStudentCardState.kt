package id.usecase.assessment.presentation.screens.class_room.create.students

import androidx.compose.foundation.text.input.TextFieldState

data class AddStudentCardState(
    val identifier: TextFieldState = TextFieldState(),
    val name: TextFieldState = TextFieldState(),
)
