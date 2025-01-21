package id.usecase.assessment.presentation.screens.class_room.create.categories

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.CategoryRepository
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.categories.components.CategoryItemState
import id.usecase.assessment.presentation.utils.toDomainForm
import id.usecase.assessment.presentation.utils.toItemState
import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCategoriesViewModel(
    private val application: Application,
    private val repository: CategoryRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _events = Channel<AddCategoriesEvent>()
    val events = _events.receiveAsFlow()

    var state = mutableStateOf(AddCategoriesState())
        private set

    fun onAction(action: AddCategoriesAction) {
        when (action) {
            is AddCategoriesAction.LoadCategories -> {
                loadCategories(classRoomId = action.classRoomId)
            }

            is AddCategoriesAction.AddCategories -> {
                addCategories(
                    categories = action.categories,
                    classRoomId = action.classRoomId
                )
            }
        }
    }

    private fun loadCategories(classRoomId: Int) {
        viewModelScope.launch(dispatcher) {
            repository.getCategoriesByClassRoomId(classRoomId)
                .catch { e ->
                    Log.e(TAG, "loadCategories: Catch", e)
                    state.value = state.value.copy(isLoading = false)
                    _events.send(
                        AddCategoriesEvent.OnErrorOccurred(
                            message = e.message ?: application.getString(R.string.unknown_error)
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> state.value = state.value.copy(isLoading = true)

                        is DataResult.Success -> {
                            Log.d(TAG, "loadCategories: Success ${result.data}")
                            state.value = state.value.copy(
                                isLoading = false,
                                categories = result
                                    .data
                                    ?.map { it.toItemState() }
                                    ?: listOf(CategoryItemState())
                            )
                        }

                        is DataResult.Error -> {
                            Log.e(TAG, "loadCategories: Error", result.exception)
                            state.value = state.value.copy(isLoading = false)
                            _events.send(
                                AddCategoriesEvent.OnErrorOccurred(
                                    message = result.exception.message ?: application.getString(
                                        R.string.unknown_error
                                    )
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun addCategories(categories: List<CategoryItemState>, classRoomId: Int) {
        viewModelScope.launch(dispatcher) {
            val categoryList = withContext(Dispatchers.Default) {
                categories.map {
                    it.toDomainForm(classRoomId)
                }
            }

            when (val result = repository.upsertCategories(categoryList)) {
                DataResult.Loading -> {
                    state.value = state.value.copy(isLoading = true)
                }

                is DataResult.Error -> {
                    _events.send(
                        AddCategoriesEvent.OnErrorOccurred(
                            message = result.exception.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                    state.value = state.value.copy(isLoading = false)
                }

                is DataResult.Success -> {
                    _events.send(AddCategoriesEvent.OnCategoriesHasAdded)
                    state.value = state.value.copy(isLoading = false)
                }
            }
        }
    }

    companion object {
        private val TAG = AddCategoriesViewModel::class.java.simpleName
    }
}