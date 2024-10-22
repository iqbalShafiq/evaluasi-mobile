package id.usecase.assessment.presentation.screens.class_room.create

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.AssessmentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CreateClassRoomViewModel(
    private val repository: AssessmentRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _events = Channel<CreateClassRoomEvent>()
    val events = _events.receiveAsFlow()

    var state = mutableStateOf(CreateClassRoomState())
        private set

    fun onAction(action: CreateClassRoomAction) {
        when (action) {
            CreateClassRoomAction.CreateClassRoom -> createClassRoom()
            is CreateClassRoomAction.SetStartDate -> state.value = state.value.copy(
                startDate = TextFieldState(
                    initialText = action.startDate
                )
            )
        }
    }

    private fun createClassRoom() {
        viewModelScope.launch(dispatcher) {
            state.value = state.value.copy(isLoading = true)
//            val startPeriodInLong = LocalDateTime.parse(state.value.startDate.text.toString()).getLong()
//
//            repository.upsertClassRoom(
//                ClassRoom(
//                    name = state.value.classRoomName.text.toString(),
//                    subject = state.value.subject.text.toString(),
//                    startPeriod = LocalDateTime.parse(state.value.startDate.text.toString()),
//                    endPeriod = null,
//                    createdTime = LocalDateTime.now(),
//                    lastModifiedTime = LocalDateTime.now()
//                )
//            ).catch { error ->
//                _events.send(CreateClassRoomEvent.OnErrorOccurred(error.message ?: ""))
//            }.collectLatest { result ->
//                state.value = state.value.copy(isLoading = false)
//                _events.send(CreateClassRoomEvent.OnClassRoomCreated(result.toUi()))
//            }
        }
    }
}