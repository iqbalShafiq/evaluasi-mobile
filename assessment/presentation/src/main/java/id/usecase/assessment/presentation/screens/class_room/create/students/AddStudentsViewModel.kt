package id.usecase.assessment.presentation.screens.class_room.create.students

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.StudentRepository
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentItemState
import id.usecase.assessment.presentation.utils.toDomainForm
import id.usecase.assessment.presentation.utils.toItemState
import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddStudentsViewModel(
    private val application: Application,
    private val dispatcher: CoroutineDispatcher,
    private val repository: StudentRepository
) : ViewModel() {
    private val _events = Channel<AddStudentsEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(AddStudentsState())
    val state = _state.asStateFlow()

    fun onAction(action: AddStudentsAction) {
        when (action) {
            is AddStudentsAction.LoadStudents -> {
                loadStudents(action.classRoomId)
            }

            is AddStudentsAction.AddStudents -> {
                submitStudents(
                    students = action.students,
                    classRoomId = action.classRoomId
                )
            }
        }
    }

    private fun loadStudents(classRoomId: Int) {
        viewModelScope.launch(dispatcher) {
            repository.getStudentsByClassRoomId(classRoomId = classRoomId)
                .catch { e ->
                    _state.value = state.value.copy(isLoading = false)
                    _events.send(
                        AddStudentsEvent.OnErrorOccurred(
                            message = e.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.value = state.value.copy(isLoading = true)

                        is DataResult.Success -> {
                            _state.value = state.value.copy(
                                isLoading = false,
                                studentList = result.data!!.map {
                                    it.toItemState()
                                }.ifEmpty { listOf(AddStudentItemState()) }
                            )
                        }

                        is DataResult.Error -> {
                            _state.value = state.value.copy(isLoading = false)
                            _events.send(
                                AddStudentsEvent.OnErrorOccurred(
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

    private fun submitStudents(students: List<AddStudentItemState>, classRoomId: Int) {
        viewModelScope.launch(dispatcher) {
            val studentList = withContext(dispatcher) {
                students.map {
                    it.toDomainForm(classRoomId)
                }
            }

            val result = repository.upsertStudents(studentList)

            when (result) {
                DataResult.Loading -> _state.value = state.value.copy(isLoading = true)
                is DataResult.Success -> {
                    _state.value = state.value.copy(isLoading = false)
                    _events.send(AddStudentsEvent.OnStudentsHasAdded)
                }

                is DataResult.Error -> {
                    _state.value = state.value.copy(isLoading = false)
                    _events.send(
                        AddStudentsEvent.OnErrorOccurred(
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