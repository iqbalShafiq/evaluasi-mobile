package id.usecase.assessment.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.presentation.utils.toDomainForm
import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val assessmentRepository: AssessmentRepository,
    private val classRoomRepository: ClassRoomRepository
) : ViewModel() {
    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    var state by mutableStateOf(HomeState())
        private set

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadClassRoom -> loadClassRoom()
        }
    }

    private fun loadClassRoom() {
        viewModelScope.launch {
            classRoomRepository.getClassRooms()
                .catch { e ->
                    state = state.copy(isLoading = false)
                    _events.send(HomeEvent.OnErrorOccurred(e))
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> {
                            state = state.copy(isLoading = true)
                        }

                        is DataResult.Success -> {
                            state = state.copy(
                                isLoading = false,
                                classRooms = result.data.map { it.toDomainForm() }
                            )
                        }

                        is DataResult.Error -> {
                            state = state.copy(isLoading = false)
                            _events.send(HomeEvent.OnErrorOccurred(result.exception))
                        }
                    }
                }
        }
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }
}