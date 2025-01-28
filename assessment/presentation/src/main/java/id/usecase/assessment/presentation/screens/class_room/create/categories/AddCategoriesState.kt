package id.usecase.assessment.presentation.screens.class_room.create.categories

import id.usecase.assessment.presentation.screens.class_room.create.categories.components.CategoryItemState
import id.usecase.core.domain.assessment.model.assessment.category.Category

data class AddCategoriesState(
    val isLoading: Boolean = false,
    val categoryFields: List<CategoryItemState> = listOf(CategoryItemState()),
    val categories: List<Category> = emptyList()
)
