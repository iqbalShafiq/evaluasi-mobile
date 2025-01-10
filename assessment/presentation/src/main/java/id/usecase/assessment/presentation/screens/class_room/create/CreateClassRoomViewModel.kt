package id.usecase.assessment.presentation.screens.class_room.create

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.CreateClassRoomEvent.OnClassRoomCreated
import id.usecase.assessment.presentation.screens.class_room.create.CreateClassRoomEvent.OnErrorOccurred
import id.usecase.assessment.presentation.utils.toUi
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
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

    private val _state = MutableStateFlow(CreateClassRoomState())
    val state = _state.asStateFlow()

    fun onAction(action: CreateClassRoomAction) {
        when (action) {
            is CreateClassRoomAction.LoadClassRoomDetail -> loadClassRoomDetail(action.classRoomId)
            CreateClassRoomAction.CreateClassRoom -> createClassRoom()
            is CreateClassRoomAction.SetStartDate -> _state.value = _state.value.copy(
                startDate = action.text
            )

            is CreateClassRoomAction.SetDescription -> _state.value = _state.value.copy(
                description = action.text
            )

            is CreateClassRoomAction.SetClassRoomName -> _state.value = _state.value.copy(
                classRoomName = action.text
            )

            is CreateClassRoomAction.SetEndDate -> _state.value = _state.value.copy(
                endDate = action.text
            )

            is CreateClassRoomAction.SetSubject -> _state.value = _state.value.copy(
                subject = action.text
            )
        }
    }

    private fun loadClassRoomDetail(classRoomId: Int) {
        viewModelScope.launch(dispatcher) {
            repository.getClassRoomById(classRoomId)
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false)
                    _events.send(
                        OnErrorOccurred(
                            e.message ?: application.getString(R.string.unknown_error)
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.value = _state.value.copy(isLoading = true)
                        is DataResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                classRoom = result.data,
                                classRoomName = TextFieldValue(
                                    text = result.data?.name ?: ""
                                ),
                                subject = TextFieldValue(
                                    text = result.data?.subject ?: ""
                                ),
                                startDate = TextFieldValue(
                                    text = result.data?.startPeriod.toString()
                                ),
                                endDate = TextFieldValue(
                                    text = result.data?.endPeriod?.toString() ?: ""
                                )
                            )
                        }

                        is DataResult.Error -> TODO()
                    }
                }
        }
    }

    private fun createClassRoom() {
        viewModelScope.launch(dispatcher) {
            _state.value = _state.value.copy(isLoading = true)
            val result = repository.upsertClassRoom(
                ClassRoom(
                    id = _state.value.classRoom?.id ?: 0,
                    name = _state.value.classRoomName.text,
                    subject = _state.value.subject.text,
                    startPeriod = LocalDate
                        .parse(_state.value.startDate.text)
                        .atTime(0, 0, 0, 0)
                        .toEpochSecond(ZoneOffset.UTC),
                    endPeriod = if (_state.value.endDate.text.isNotEmpty()) LocalDate
                        .parse(_state.value.endDate.text)
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
                    _state.value = _state.value.copy(isLoading = true)
                }

                is DataResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                    result.data?.toUi()?.let {
                        _events.send(OnClassRoomCreated(it))
                    }
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false)
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