package id.usecase.assessment.presentation.screens.class_room.create.categories

data class AddCategoriesState(
    val isLoading: Boolean = false,
    val categories: List<CategoryItemState> = emptyList(),
)
