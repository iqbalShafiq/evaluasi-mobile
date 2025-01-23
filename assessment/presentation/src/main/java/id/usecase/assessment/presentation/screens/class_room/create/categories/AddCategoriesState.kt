package id.usecase.assessment.presentation.screens.class_room.create.categories

import id.usecase.assessment.presentation.screens.class_room.create.categories.components.CategoryItemState

data class AddCategoriesState(
    val isLoading: Boolean = false,
    val categories: List<CategoryItemState> = listOf(CategoryItemState()),
)
