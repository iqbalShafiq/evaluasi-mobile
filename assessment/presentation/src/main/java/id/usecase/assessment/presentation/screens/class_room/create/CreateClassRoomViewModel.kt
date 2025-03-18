package id.usecase.assessment.presentation.screens.class_room.create

import android.app.Application
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.CreateClassRoomEvent.OnClassRoomCreated
import id.usecase.assessment.presentation.screens.class_room.create.CreateClassRoomEvent.OnErrorOccurred
import id.usecase.assessment.presentation.utils.toUi
import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale

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
            is CreateClassRoomAction.UpdateTextField -> {
                _state.update { action.state }
                if (
                    _state.value.classRoomName.text.isNotEmpty() &&
                    _state.value.subject.text.isNotEmpty() &&
                    _state.value.startDate.text.isNotEmpty()
                ) {
                    _state.update {
                        it.copy(isFormValid = true)
                    }
                } else {
                    _state.update {
                        it.copy(isFormValid = false)
                    }
                }
            }

            CreateClassRoomAction.CreateClassRoom -> upsertClassRoom()
        }
    }

    private fun loadClassRoomDetail(classRoomId: String) {
        viewModelScope.launch(dispatcher) {
            repository.getClassRoomById(classRoomId)
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false)
                    }

                    _events.send(
                        OnErrorOccurred(
                            e.message ?: application.getString(R.string.unknown_error)
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.update {
                            it.copy(isLoading = true)
                        }

                        is DataResult.Success -> {
                            Log.d("TAG", "loadClassRoomDetail: ${result.data?.startPeriod}")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isEditing = true,
                                    classRoom = result.data,
                                    classRoomName = TextFieldValue(
                                        text = result.data?.name ?: ""
                                    ),
                                    subject = TextFieldValue(
                                        text = result.data?.subject ?: ""
                                    ),
                                    startDate = TextFieldValue(
                                        text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                            .format((result.data?.startPeriod ?: 0L) * 1_000)
                                    ),
                                    longPeriod = TextFieldValue(
                                        text = result.data?.longPeriod?.toString() ?: ""
                                    ),
                                    description = TextFieldValue(
                                        text = result.data?.description ?: ""
                                    ),
                                    hasSchedule = result.data?.schedule?.isNotEmpty() ?: false,
                                    selectedDays = result.data?.schedule
                                        ?.mapNotNull { day ->
                                            DayOfWeek.entries.find { dow -> dow.value == day }
                                        }
                                        ?.toSet()
                                        ?: emptySet()
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun upsertClassRoom() {
        viewModelScope.launch(dispatcher) {
            val result = repository.upsertClassRoom(
                ClassRoom(
                    id = _state.value.classRoom?.id ?: "",
                    name = _state.value.classRoomName.text,
                    subject = _state.value.subject.text,
                    description = _state.value.description.text,
                    startPeriod = LocalDate
                        .parse(_state.value.startDate.text)
                        .atTime(0, 0, 0, 0)
                        .toEpochSecond(ZoneOffset.UTC),
                    longPeriod = _state.value.longPeriod.text.toLongOrNull(),
                    createdTime = LocalDateTime
                        .now()
                        .toEpochSecond(ZoneOffset.UTC),
                    lastModifiedTime = LocalDateTime
                        .now()
                        .toEpochSecond(ZoneOffset.UTC),
                    schedule = _state.value.selectedDays.map { it.value }
                )
            )

            when (result) {
                DataResult.Loading -> {
                    Log.d("TAG", "upsertClassRoom: Loading ...")
                    _state.update { it.copy(isLoading = true) }
                }

                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false) }

                    val classRoom = result.data?.toUi()
                    if (classRoom != null) {
                        _events.send(OnClassRoomCreated(classRoom))
                        return@launch
                    }

                    _events.send(
                        CreateClassRoomEvent.OnClassRoomHasUpdated
                    )
                }
            }
        }
    }
}