package id.usecase.assessment.presentation.screens.class_room.create.students.item

import androidx.compose.foundation.text.input.TextFieldState

data class AddStudentItemState(
    val identifier: TextFieldState = TextFieldState(),
    val name: TextFieldState = TextFieldState(),
)
