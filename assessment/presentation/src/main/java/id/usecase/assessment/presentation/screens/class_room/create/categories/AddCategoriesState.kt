package id.usecase.assessment.presentation.screens.class_room.create.categories

import id.usecase.assessment.presentation.screens.class_room.create.categories.item.CategoryItemState

data class AddCategoriesState(
    val isLoading: Boolean = false,
    val categories: List<CategoryItemState> = emptyList(),
)
