package id.usecase.assessment.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.domain.StudentRepository
import id.usecase.assessment.presentation.utils.toUi
import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val classRoomRepository: ClassRoomRepository,
    private val studentRepository: StudentRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        onAction(action = HomeAction.LoadClassRoom)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadClassRoom -> {
                viewModelScope.launch(dispatcher) {
                    loadStudentTotal()
                    loadClassRoom()
                }
            }

            is HomeAction.UpdateTextField -> _state.update { action.state }
        }
    }

    private suspend fun loadStudentTotal() {
        withContext(dispatcher) {
            studentRepository.getTotalStudent()
                .catch {
                    _state.update {
                        it.copy(totalStudent = 0)
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is DataResult.Success -> _state.update {
                            it.copy(totalStudent = result.data)
                        }

                        else -> _state.update {
                            it.copy(totalStudent = 0)
                        }
                    }
                }
        }
    }

    private suspend fun loadClassRoom() {
        withContext(dispatcher) {
            classRoomRepository.getClassRooms()
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    _events.send(HomeEvent.OnErrorOccurred(e))
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.update { it.copy(isLoading = true) }

                        is DataResult.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    classRooms = result.data.map { classRoom ->
                                        classRoom.toUi().copy(
                                            studentCount = loadStudentTotalPerClassRoom(
                                                classRoom.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
        }
    }

    private suspend fun loadStudentTotalPerClassRoom(classRoomId: Int): Int {
        return withContext(dispatcher) {
            studentRepository.getStudentsByClassRoomId(classRoomId)
                .catch { 0 }
                .map { result ->
                    when (result) {
                        is DataResult.Success -> result.data.size
                        else -> 0
                    }
                }
                .last()
        }
    }
}