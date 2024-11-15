package id.usecase.assessment.presentation.screens.assessment

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.CategoryRepository
import id.usecase.assessment.domain.EventRepository
import id.usecase.assessment.domain.StudentRepository
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.StudentScoreUi
import id.usecase.assessment.presentation.screens.assessment.AssessmentEvent.*
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.student.Student
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AssessmentViewModel(
    private val application: Application,
    private val dispatcher: CoroutineDispatcher,
    private val studentRepository: StudentRepository,
    private val eventRepository: EventRepository,
    private val categoryRepository: CategoryRepository,
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {
    private val _events = Channel<AssessmentEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(AssessmentState())
    val state: StateFlow<AssessmentState> = _state

    fun onAction(action: AssessmentAction) {
        when (action) {
            is AssessmentAction.LoadAssessmentDetail -> loadAssessmentDetail(
                classRoomId = action.classRoomId,
                eventId = action.eventId
            )

            is AssessmentAction.SaveAssessmentEvent -> saveEvent(action.assessmentEvent)

            is AssessmentAction.DeleteAssessmentEvent -> deleteAssessmentEvent(action.eventId)
        }
    }

    private suspend fun loadStudentsOfClassRoom(classRoomId: Int) {
        withContext(dispatcher) {
            studentRepository.getStudentsByClassRoomId(classRoomId)
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false)
                    _events.send(
                        OnErrorOccurred(
                            e.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.value = _state.value.copy(isLoading = true)

                        is DataResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                studentList = result.data
                            )
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

    private suspend fun loadAssessmentsByEventId(eventId: Int) {
        withContext(dispatcher) {
            assessmentRepository.getAssessmentsByEventId(eventId)
                .catch { e ->
                    onAssessmentHasNotCreatedYet()
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.value = _state.value.copy(
                            isLoading = true
                        )

                        is DataResult.Success -> _state.value = _state.value.copy(
                            isLoading = false,
                            assessmentList = result.data
                        )

                        is DataResult.Error -> onAssessmentHasNotCreatedYet()
                    }
                }
        }
    }

    private suspend fun onAssessmentHasNotCreatedYet() {
        withContext(dispatcher) {
            _state.value = _state.value.copy(
                isLoading = false,
                assessmentList = emptyList()
            )
        }
    }

    private suspend fun getStudentAvgScore(studentId: Int): Double {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(dispatcher) {
                assessmentRepository.getAverageScoreByStudentId(studentId)
                    .catch { e ->
                        continuation.resume(0.0)
                    }
                    .collectLatest { result ->
                        when (result) {
                            is DataResult.Success -> continuation.resume(result.data)
                            else -> continuation.resume(0.0)
                        }
                    }
            }
        }
    }

    private suspend fun transformAssessmentToStudentAssessmentState(
        assessmentList: List<Assessment>,
        studentList: List<Student>
    ): List<StudentAssessmentState> {
        return withContext(Dispatchers.Default) {
            studentList.map { student ->
                val studentAssessment = assessmentList
                    .find { it.studentId == student.id }

                val avgScore = getStudentAvgScore(student.id)

                StudentAssessmentState(
                    data = StudentScoreUi(
                        assessmentId = studentAssessment?.id ?: 0,
                        studentId = student.id,
                        studentName = student.name,
                        score = studentAssessment?.score ?: 0.0,
                        avgScore = avgScore,
                        comments = ""
                    ),
                    score = TextFieldState(
                        initialText = studentAssessment?.score?.toString() ?: ""
                    )
                )
            }
        }
    }

    private suspend fun loadCategoryById(categoryId: Int): Category? {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(dispatcher) {
                categoryRepository.getCategoryById(categoryId)
                    .catch { e ->
                        continuation.resume(null)
                    }
                    .collectLatest { result ->
                        when (result) {
                            is DataResult.Success -> continuation.resume(result.data)
                            else -> continuation.resume(null)
                        }
                    }
            }
        }
    }

    private fun loadAssessmentDetail(classRoomId: Int, eventId: Int) {
        viewModelScope.launch(dispatcher) {
            loadStudentsOfClassRoom(classRoomId)
            loadAssessmentsByEventId(eventId)

            eventRepository.getEventById(eventId)
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false)
                    _events.send(
                        OnErrorOccurred(
                            e.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.value = _state.value.copy(isLoading = true)

                        is DataResult.Success -> {
                            val assessmentListField = transformAssessmentToStudentAssessmentState(
                                _state.value.assessmentList,
                                _state.value.studentList
                            )

                            val formattedEventDate = SimpleDateFormat(
                                "dd MMMM yyyy",
                                Locale.getDefault()
                            ).format(result.data?.eventDate)

                            val category = loadCategoryById(result.data?.categoryId ?: -1)

                            _state.value = _state.value.copy(
                                isLoading = false,
                                assessmentNameField = TextFieldState(
                                    initialText = result.data?.name ?: ""
                                ),
                                startDateField = TextFieldState(
                                    initialText = formattedEventDate
                                ),
                                categoryField = TextFieldState(
                                    initialText = category?.name ?: ""
                                ),
                                assessmentListField = assessmentListField
                            )
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

    private fun saveEvent(event: Event) {
        viewModelScope.launch(dispatcher) {
            val result = eventRepository.upsertEvent(event)
            when (result) {
                DataResult.Loading -> _state.value = _state.value.copy(isLoading = true)

                is DataResult.Success -> {
                    saveAssessments(result.data?.id ?: -1)
                    _state.value = _state.value.copy(isLoading = false)
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

    private suspend fun saveAssessments(eventId: Int) {
        withContext(dispatcher) {
            val assessmentList = withContext(Dispatchers.Default) {
                _state.value.assessmentListField.map { assessment ->
                    Assessment(
                        id = assessment.data?.assessmentId ?: 0,
                        studentId = assessment.data?.studentId ?: 0,
                        eventId = eventId,
                        score = assessment.data?.score ?: 0.0,
                        createdTime = System.currentTimeMillis(),
                        lastModifiedTime = System.currentTimeMillis()
                    )
                }
            }

            val result = assessmentRepository.upsertAssessments(assessmentList)
            when (result) {
                DataResult.Loading -> _state.value = _state.value.copy(isLoading = true)

                is DataResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _events.send(AssessmentHasSaved)
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

    private suspend fun getEventById(eventId: Int): Event? {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(dispatcher) {
                eventRepository.getEventById(eventId)
                    .catch { e ->
                        continuation.resume(null)
                    }
                    .collectLatest { result ->
                        when (result) {
                            is DataResult.Success -> continuation.resume(result.data)
                            else -> continuation.resume(null)
                        }
                    }
            }
        }
    }

    private fun deleteAssessmentEvent(eventId: Int) {
        viewModelScope.launch(dispatcher) {
            val event = getEventById(eventId)

            if (event == null) {
                _events.send(
                    OnErrorOccurred(
                        application.getString(R.string.event_not_found)
                    )
                )
                return@launch
            }

            eventRepository.deleteEvent(event)
            _events.send(AssessmentHasSaved)
        }
    }
}