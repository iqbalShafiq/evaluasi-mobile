package id.usecase.assessment.presentation.screens.class_room.create.students.components

import androidx.compose.ui.text.input.TextFieldValue

data class AddStudentItemState(
    val id: Int = 0,
    val identifier: TextFieldValue = TextFieldValue(),
    val name: TextFieldValue = TextFieldValue(),
    val isValid: Boolean = false
)
