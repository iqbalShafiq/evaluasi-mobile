package id.usecase.assessment.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.presentation.utils.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AssessmentRepository
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadClassRoom -> loadClassRoom()

            is HomeAction.OpenClassRoom -> TODO()

            HomeAction.AddClassRoom -> TODO()
        }
    }

    private fun loadClassRoom() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            repository.getClassRooms()
                .map {
                    state = state.copy(
                        isLoading = false,
                        classRooms = it.map { it.toUi() }
                    )
                }
                .catch {
                    state = state.copy(isLoading = false)
                    eventChannel.send(HomeEvent.OnError(it))
                }
                .collect()
        }
    }
}