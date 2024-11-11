package id.usecase.assessment.presentation.screens.class_room.create.categories

sealed class AddCategoriesEvent {
    data class OnErrorOccurred(val message: String) : AddCategoriesEvent()
    data object OnCategoriesHasAdded : AddCategoriesEvent()
}