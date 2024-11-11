package id.usecase.assessment.presentation.screens.class_room.create

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.CreateClassRoomEvent.*
import id.usecase.assessment.presentation.utils.toUi
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateClassRoomViewModel(
    private val application: Application,
    private val repository: ClassRoomRepository,
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
            val result = repository.upsertClassRoom(
                ClassRoom(
                    id = 0,
                    name = state.value.classRoomName.text.toString(),
                    subject = state.value.subject.text.toString(),
                    startPeriod = LocalDate
                        .parse(state.value.startDate.text.toString())
                        .atTime(0, 0, 0, 0)
                        .toEpochSecond(ZoneOffset.UTC),
                    endPeriod = if (state.value.endDate.text.isNotEmpty()) LocalDate
                        .parse(state.value.endDate.text.toString())
                        .atTime(23, 59, 59)
                        .toEpochSecond(ZoneOffset.UTC) else null,
                    createdTime = LocalDateTime
                        .now()
                        .toEpochSecond(ZoneOffset.UTC),
                    lastModifiedTime = LocalDateTime
                        .now()
                        .toEpochSecond(ZoneOffset.UTC)
                )
            )

            when (result) {
                DataResult.Loading -> {
                    state.value = state.value.copy(isLoading = true)
                }

                is DataResult.Success -> {
                    state.value = state.value.copy(isLoading = false)
                    result.data?.toUi()?.let {
                        _events.send(OnClassRoomCreated(it))
                    }
                }

                is DataResult.Error -> {
                    state.value = state.value.copy(isLoading = false)
                    _events.send(
                        OnErrorOccurred(
                            result.exception.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
            }
        }
    }
}