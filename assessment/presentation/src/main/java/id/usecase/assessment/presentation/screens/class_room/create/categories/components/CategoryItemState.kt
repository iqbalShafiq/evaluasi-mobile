package id.usecase.assessment.presentation.screens.class_room.create.categories.components

import androidx.compose.ui.text.input.TextFieldValue

data class CategoryItemState(
    val id: String = "",
    val name: TextFieldValue = TextFieldValue(),
    val partPercentage: TextFieldValue = TextFieldValue(),
    val description: TextFieldValue = TextFieldValue(),
)
