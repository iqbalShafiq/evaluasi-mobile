package id.usecase.assessment.presentation.screens.class_room.create.categories

import id.usecase.assessment.presentation.screens.class_room.create.categories.components.CategoryItemState

sealed class AddCategoriesAction {
    data class LoadCategories(val classRoomId: Int) : AddCategoriesAction()
    data class AddCategories(
        val categories: List<CategoryItemState>,
        val classRoomId: Int
    ) : AddCategoriesAction()
}