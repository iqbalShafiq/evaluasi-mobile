package id.usecase.assessment.presentation.screens.assessment

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.CategoryRepository
import id.usecase.assessment.domain.EventRepository
import id.usecase.assessment.domain.StudentRepository
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.StudentScoreUi
import id.usecase.assessment.presentation.screens.assessment.AssessmentEvent.AssessmentHasSaved
import id.usecase.assessment.presentation.screens.assessment.AssessmentEvent.OnErrorOccurred
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.student.Student
import id.usecase.core.domain.assessment.utils.FlowResultHandler
import id.usecase.core.domain.assessment.utils.handleSuccessResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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
) : ViewModel(), FlowResultHandler {
    private val _events = Channel<AssessmentEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(AssessmentState())
    var state = _state.asStateFlow()

    fun onAction(action: AssessmentAction) {
        when (action) {
            is AssessmentAction.LoadAssessmentDetail -> loadAssessmentDetail(
                classRoomId = action.classRoomId,
                eventId = action.eventId
            )

            is AssessmentAction.SaveAssessmentEvent -> saveEvent(assessments = action.assessments)

            AssessmentAction.DeleteAssessmentEvent -> deleteAssessmentEvent()

            is AssessmentAction.UpdateForms -> {
                _state.update { action.updatedState }
            }
        }
    }

    private suspend fun loadStudentsOfClassRoom(classRoomId: Int) {
        withContext(dispatcher) {
            studentRepository.getStudentsByClassRoomId(classRoomId)
                .catch { e ->
                    _state.update { it.copy(isLoading = false) }
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
                        DataResult.Loading -> _state.update { it.copy(isLoading = true) }

                        is DataResult.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    studentList = result.data ?: emptyList()
                                )
                            }
                        }

                        is DataResult.Error -> {
                            _state.update { it.copy(isLoading = false) }
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
                .catch {
                    onAssessmentHasNotCreatedYet()
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.update { it.copy(isLoading = true) }

                        is DataResult.Success -> _state.update {
                            it.copy(
                                isLoading = false,
                                assessmentList = result.data ?: emptyList()
                            )
                        }

                        is DataResult.Error -> onAssessmentHasNotCreatedYet()
                    }
                }
        }
    }

    private suspend fun onAssessmentHasNotCreatedYet() {
        withContext(dispatcher) {
            _state.update {
                it.copy(
                    isLoading = false,
                    assessmentList = emptyList()
                )
            }
        }
    }

    private suspend fun getStudentAvgScore(studentId: Int): Double {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(dispatcher) {
                assessmentRepository.getAverageScoreByStudentId(studentId)
                    .catch {
                        continuation.resume(0.0)
                    }
                    .collectLatest { result ->
                        when (result) {
                            is DataResult.Success -> continuation.resume(
                                value = result.data ?: 0.0
                            )

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
                    score = TextFieldValue(
                        text = studentAssessment?.score?.toString() ?: ""
                    )
                )
            }
        }
    }

    private suspend fun loadCategoryById(categoryId: Int): Category? {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(dispatcher) {
                categoryRepository.getCategoryById(categoryId)
                    .catch {
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
                .handleSuccessResult(
                    listener = this@AssessmentViewModel,
                    retryCallback = { loadAssessmentDetail(classRoomId, eventId) },
                    successCallback = { result ->
                        val assessmentListField = transformAssessmentToStudentAssessmentState(
                            state.value.assessmentList,
                            state.value.studentList
                        )

                        val formattedEventDate = SimpleDateFormat(
                            "dd MMMM yyyy",
                            Locale.getDefault()
                        ).format(result.data?.eventDate)

                        val category = loadCategoryById(result.data?.categoryId ?: -1)

                        _state.update {
                            it.copy(
                                isLoading = false,
                                assessmentEvent = result.data,
                                assessmentNameField = TextFieldValue(
                                    text = result.data?.name ?: ""
                                ),
                                startDateField = TextFieldValue(
                                    text = formattedEventDate
                                ),
                                categoryField = TextFieldValue(
                                    text = category?.name ?: ""
                                ),
                                assessmentListField = assessmentListField
                            )
                        }
                    }
                )
        }
    }

    private fun saveEvent(assessments: List<StudentAssessmentState>) {
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(assessmentListField = assessments) }

            val event = Event(
                id = state.value.assessmentEvent?.id ?: 0,
                name = state.value.assessmentNameField.text,
                eventDate = System.currentTimeMillis(),
                categoryId = state.value.category?.id ?: 0,
                createdTime = System.currentTimeMillis(),
                lastModifiedTime = System.currentTimeMillis()
            )

            when (val result = eventRepository.upsertEvent(event)) {
                DataResult.Loading -> _state.update { it.copy(isLoading = true) }

                is DataResult.Success -> {
                    saveAssessments(result.data?.id ?: -1)
                    _state.update { it.copy(isLoading = false) }
                }

                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(
                        OnErrorOccurred(
                            errorMessage = result.exception.message ?: application.getString(
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
                state.value.assessmentListField.map { assessment ->
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

            when (val result = assessmentRepository.upsertAssessments(assessmentList)) {
                DataResult.Loading -> _state.update { it.copy(isLoading = true) }

                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(AssessmentHasSaved)
                }

                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(
                        OnErrorOccurred(
                            errorMessage = result.exception.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }

            }

        }
    }

    private fun deleteAssessmentEvent() {
        viewModelScope.launch(dispatcher) {
            val event = state.value.assessmentEvent

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

    override suspend fun onLoading() {
        _state.update {
            it.copy(isLoading = true)
        }
    }

    override suspend fun onErrorOccurred(errorMessage: String?, retryCallback: (() -> Unit)?) {
        _state.update {
            it.copy(isLoading = false)
        }

        _events.send(
            OnErrorOccurred(
                errorMessage = errorMessage ?: application.getString(
                    R.string.unknown_error
                )
            )
        )
    }
}