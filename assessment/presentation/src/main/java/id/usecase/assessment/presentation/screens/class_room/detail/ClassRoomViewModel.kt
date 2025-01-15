package id.usecase.assessment.presentation.screens.class_room.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.FloatEntry
import id.usecase.assessment.domain.AnalyticsRepository
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.CategoryRepository
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.domain.EventRepository
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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
    private val assessmentRepository: AssessmentRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    private val _events = Channel<ClassRoomEvent>()
    val events = _events.receiveAsFlow()

    private var _state = MutableStateFlow(ClassRoomState())
    val state = _state.asStateFlow()

    fun onAction(action: ClassRoomAction) {
        when (action) {
            is ClassRoomAction.LoadClassRoom -> {
                viewModelScope.launch(dispatcher) {
                    loadClassRoom(action.classRoomId)
                    loadAssessmentEvents()
                    getPerformanceTrend()
                    getCategoryDistribution()
                    getPerformanceDistribution()
                }
            }
        }
    }

    private suspend fun loadClassRoom(classRoomId: Int) {
        withContext(dispatcher) {
            classRoomRepository.getClassRoomById(classRoomId)
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false)
                    }

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
                        DataResult.Loading -> _state.update {
                            it.copy(isLoading = true)
                        }

                        is DataResult.Success -> {
                            _state.update {
                                it.copy(classRoom = result.data)
                            }
                        }

                        is DataResult.Error -> {
                            _state.update {
                                it.copy(isLoading = false)
                            }

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

    private suspend fun loadAssessmentEvents() {
        withContext(dispatcher) {
            val classRoomId = state.value.classRoom?.id ?: return@withContext
            eventRepository.getEventsByClassRoomId(classRoomId)
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false)
                    }

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
                        DataResult.Loading -> _state.update {
                            it.copy(isLoading = true)
                        }

                        is DataResult.Success -> {
                            _state.update {
                                it.copy(events = result.data)
                            }

                            val categoryNames = getCategoryNames(classRoomId)

                            val assessmentEventUiList = result.data.map { event ->
                                val categoryName = categoryNames
                                    .find { it.first == event.categoryId }
                                    ?.second ?: ""

                                AssessmentEventUi(
                                    id = event.id,
                                    name = event.name,
                                    totalAssessment = 0,
                                    categoryId = event.categoryId,
                                    categoryName = categoryName,
                                    classId = classRoomId,
                                    eventDate = event.eventDate.toString(),
                                    createdTime = event.createdTime.toString(),
                                    lastModifiedTime = event.lastModifiedTime.toString()
                                )
                            }

                            _state.update {
                                it.copy(assessmentEvents = assessmentEventUiList)
                            }

                            val assessmentEventUiListWithTotalAssessment = assessmentEventUiList
                                .map { assessmentEventUi ->
                                    val totalAssessment = getTotalAssessmentFromEvent(
                                        eventId = assessmentEventUi.id
                                    )
                                    assessmentEventUi.copy(totalAssessment = totalAssessment)
                                }

                            _state.update {
                                it.copy(
                                    assessmentEvents = assessmentEventUiListWithTotalAssessment,
                                    isLoading = false
                                )
                            }
                        }

                        is DataResult.Error -> {
                            _state.update {
                                it.copy(isLoading = false)
                            }

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
                    .catch {
                        continuation.resume(emptyList())
                    }
                    .collectLatest { result ->
                        when (result) {
                            DataResult.Loading -> {
                                _state.update {
                                    it.copy(isLoading = true)
                                }
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

    private suspend fun getTotalAssessmentFromEvent(eventId: Int): Int {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(dispatcher) {
                assessmentRepository.getAssessmentsByEventId(eventId)
                    .catch {
                        continuation.resume(0)
                    }
                    .collectLatest { result ->
                        when (result) {
                            DataResult.Loading -> {
                                _state.update {
                                    it.copy(isLoading = true)
                                }
                            }

                            is DataResult.Success -> {
                                continuation.resume(result.data.size)
                            }

                            is DataResult.Error -> {
                                continuation.resume(0)
                            }
                        }
                    }
            }
        }
    }

    private suspend fun getPerformanceTrend() {
        withContext(dispatcher) {
            val classRoomId = state.value.classRoom?.id ?: 0
            if (classRoomId == 0) return@withContext

            analyticsRepository.getPerformanceTrend(classRoomId)
                .catch { e ->
                    _events.send(
                        ClassRoomEvent.OnErrorOccurred(
                            message = e.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    _state.update {
                        it.copy(
                            performanceTrendData = result.map { trend ->
                                FloatEntry(trend.first, trend.second)
                            }
                        )
                    }
                }
        }
    }

    private suspend fun getCategoryDistribution() {
        withContext(dispatcher) {
            val classRoomId = state.value.classRoom?.id ?: 0
            if (classRoomId == 0) return@withContext

            analyticsRepository.getCategoryDistribution(classRoomId)
                .catch { e ->
                    _events.send(
                        ClassRoomEvent.OnErrorOccurred(
                            message = e.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    _state.update {
                        it.copy(
                            categoryList = result.map { category -> category.first },
                            categoryDistributionData = result.mapIndexed { index, distribution ->
                                FloatEntry(index.toFloat(), distribution.second)
                            }
                        )
                    }
                }
        }
    }

    private suspend fun getPerformanceDistribution() {
        withContext(dispatcher) {
            val classRoomId = state.value.classRoom?.id ?: 0
            if (classRoomId == 0) return@withContext

            analyticsRepository.getPerformanceDistribution(classRoomId)
                .catch { e ->
                    _events.send(
                        ClassRoomEvent.OnErrorOccurred(
                            message = e.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    _state.update {
                        it.copy(performanceDistribution = result)
                    }
                }
        }
    }
}