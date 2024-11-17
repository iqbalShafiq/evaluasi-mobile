package id.usecase.assessment.presentation.screens.class_room.detail

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.CategoryRepository
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.domain.EventRepository
import id.usecase.assessment.presentation.R
import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ClassRoomViewModel(
    private val application: Application,
    private val dispatcher: CoroutineDispatcher,
    private val classRoomRepository: ClassRoomRepository,
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {
    private val _events = Channel<ClassRoomEvent>()
    val events = _events.receiveAsFlow()

    var state = mutableStateOf(ClassRoomState())
        private set

    fun onAction(action: ClassRoomAction) {
        when (action) {
            is ClassRoomAction.LoadClassRoom -> loadClassRoom(action.classRoomId)
        }
    }

    private fun loadClassRoom(classRoomId: Int) {
        viewModelScope.launch(dispatcher) {
            classRoomRepository.getClassRoomById(classRoomId)
                .catch { e ->
                    state.value = state.value.copy(isLoading = false)
                    _events.send(
                        ClassRoomEvent.OnErrorOccurred(
                            message = e.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> state.value = state.value.copy(isLoading = true)

                        is DataResult.Success -> {
                            state.value = state.value.copy(
                                classRoom = result.data
                            )

                            loadEvents(classRoomId)
                        }

                        is DataResult.Error -> {
                            state.value = state.value.copy(isLoading = false)
                            _events.send(
                                ClassRoomEvent.OnErrorOccurred(
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

    private suspend fun loadEvents(classRoomId: Int) {
        withContext(dispatcher) {
            eventRepository.getEventsByClassRoomId(classRoomId)
                .catch { e ->
                    state.value = state.value.copy(isLoading = false)
                    _events.send(
                        ClassRoomEvent.OnErrorOccurred(
                            message = e.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> state.value = state.value.copy(isLoading = true)

                        is DataResult.Success -> {
                            state.value = state.value.copy(
                                events = result.data
                            )

                            val categoryNames = getCategoryNames(classRoomId)
                        }

                        is DataResult.Error -> {
                            state.value = state.value.copy(isLoading = false)
                            _events.send(
                                ClassRoomEvent.OnErrorOccurred(
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

    private suspend fun getCategoryNames(classRoomId: Int): List<Pair<Int, String>> {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(dispatcher) {
                categoryRepository.getCategoriesByClassRoomId(classRoomId)
                    .catch { e ->
                        continuation.resume(emptyList<Pair<Int, String>>())
                    }
                    .collectLatest { result ->
                        when (result) {
                            DataResult.Loading -> {
                                state.value = state.value.copy(isLoading = true)
                            }

                            is DataResult.Success -> {
                                val categories = result.data.map {
                                    Pair(it.id, it.name)
                                }

                                continuation.resume(categories)
                            }

                            is DataResult.Error -> {
                                continuation.resume(emptyList())
                            }
                        }
                    }
            }
        }
    }
}