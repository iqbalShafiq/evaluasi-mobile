package id.usecase.assessment.presentation.screens.class_room.create.categories

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import id.usecase.assessment.presentation.model.CategoryUi
import id.usecase.assessment.presentation.utils.toUi

class AddCategoriesViewModel : ViewModel() {
    var state = mutableStateOf(AddCategoriesState())
        private set

    fun onAction(action: AddCategoriesAction) {
        when (action) {
            is AddCategoriesAction.AddCategories -> {
                val categories = action.categories.map {
                    it.toUi()
                }

                addCategories(categories)
            }
        }
    }

    private fun addCategories(categories: List<CategoryUi>) {

    }
}