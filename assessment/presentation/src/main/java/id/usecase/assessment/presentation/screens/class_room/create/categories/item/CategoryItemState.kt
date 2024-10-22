package id.usecase.assessment.presentation.screens.class_room.create.categories.item

import androidx.compose.foundation.text.input.TextFieldState

data class CategoryItemState(
    val name: TextFieldState = TextFieldState(),
    val partPercentage: TextFieldState = TextFieldState(),
    val description: TextFieldState = TextFieldState(),
)
