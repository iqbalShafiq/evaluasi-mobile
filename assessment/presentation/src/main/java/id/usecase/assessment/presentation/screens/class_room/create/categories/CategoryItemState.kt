package id.usecase.assessment.presentation.screens.class_room.create.categories

import androidx.compose.foundation.text.input.TextFieldState

data class CategoryItemState(
    val name: TextFieldState,
    val partPercentage: TextFieldState,
    val description: TextFieldState,
)
