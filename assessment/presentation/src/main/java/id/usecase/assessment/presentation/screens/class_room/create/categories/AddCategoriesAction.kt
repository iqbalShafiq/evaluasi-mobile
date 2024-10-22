package id.usecase.assessment.presentation.screens.class_room.create.categories

import id.usecase.assessment.presentation.screens.class_room.create.categories.item.CategoryItemState

sealed class AddCategoriesAction {
    data class AddCategories(val categories: List<CategoryItemState>) : AddCategoriesAction()
}