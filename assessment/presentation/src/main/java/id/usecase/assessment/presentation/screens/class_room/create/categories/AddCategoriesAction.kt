package id.usecase.assessment.presentation.screens.class_room.create.categories

sealed class AddCategoriesAction {
    data class AddCategories(val categories: List<CategoryItemState>) : AddCategoriesAction()
}