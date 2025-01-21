package id.usecase.assessment.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.presentation.utils.toUi
import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val assessmentRepository: AssessmentRepository,
    private val classRoomRepository: ClassRoomRepository
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
            HomeAction.LoadClassRoom -> loadClassRoom()
            is HomeAction.UpdateTextField -> _state.update { action.state }
        }
    }

    private fun loadClassRoom() {
        viewModelScope.launch {
            classRoomRepository.getClassRooms()
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    _events.send(HomeEvent.OnErrorOccurred(e))
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }

                        is DataResult.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    classRooms = result.data?.map { classRoom ->
                                        classRoom.toUi()
                                    } ?: emptyList()
                                )
                            }
                        }

                        is DataResult.Error -> {
                            _state.update {
                                it.copy(isLoading = false)
                            }
                            _events.send(HomeEvent.OnErrorOccurred(result.exception))
                        }
                    }
                }
        }
    }
}