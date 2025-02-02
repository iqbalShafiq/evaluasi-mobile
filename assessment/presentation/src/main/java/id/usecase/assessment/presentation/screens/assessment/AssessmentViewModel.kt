package id.usecase.assessment.presentation.screens.assessment

import android.app.Application
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.CategoryRepository
import id.usecase.assessment.domain.EventRepository
import id.usecase.assessment.domain.SectionRepository
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
import id.usecase.core.domain.assessment.model.section.EventSection
import id.usecase.core.domain.assessment.model.student.Student
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class AssessmentViewModel(
    private val application: Application,
    private val dispatcher: CoroutineDispatcher,
    private val studentRepository: StudentRepository,
    private val eventRepository: EventRepository,
    private val categoryRepository: CategoryRepository,
    private val assessmentRepository: AssessmentRepository,
    private val sectionRepository: SectionRepository
) : ViewModel() {
    private val _events = Channel<AssessmentEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(AssessmentState())
    var state = _state.asStateFlow()

    fun onAction(action: AssessmentAction) {
        when (action) {
            is AssessmentAction.LoadAssessmentDetail -> {
                viewModelScope.launch(dispatcher) {
                    loadClassSections(action.classRoomId)
                    loadAssessmentCategories(action.classRoomId)

                    if (action.eventId != null) {
                        loadStudentsOfClassRoom(action.classRoomId)
                        loadSelectedSections(action.eventId)
                        loadAssessmentsByEventId(action.eventId)
                        loadAssessmentDetail(
                            eventId = action.eventId
                        )

                        return@launch
                    }

                    loadNewAssessment(
                        classRoomId = action.classRoomId
                    )
                }
            }

            is AssessmentAction.SaveAssessmentEvent -> saveEvent(assessments = action.assessments)

            is AssessmentAction.UpdateForms -> {
                _state.update { action.updatedState }
                checkFormValidity()
            }

            is AssessmentAction.OnCategorySelected -> {
                onCategorySelected(action.category)
                checkFormValidity()
            }

            AssessmentAction.DeleteAssessmentEvent -> deleteAssessmentEvent()
        }
    }

    private suspend fun loadClassSections(classRoomId: Int) {
        withContext(dispatcher) {
            sectionRepository.getSectionsByClassRoomId(classRoomId)
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false)
                    }
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
                        DataResult.Loading -> _state.update {
                            it.copy(isLoading = true)
                        }

                        is DataResult.Success -> {
                            _state.update {
                                it.copy(
                                    sectionList = result.data,
                                    sectionNameList = result.data.map { section -> section.name }
                                )
                            }
                        }
                    }
                }
        }
    }

    private suspend fun loadAssessmentCategories(classRoomId: Int) {
        withContext(dispatcher) {
            categoryRepository.getCategoriesByClassRoomId(classRoomId)
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false)
                    }
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
                        DataResult.Loading -> _state.update {
                            it.copy(isLoading = true)
                        }

                        is DataResult.Success -> {
                            _state.update {
                                it.copy(
                                    categoryList = result.data,
                                    categoryNameList = result.data.map { category -> category.name }
                                )
                            }
                        }
                    }
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
                            Log.d("TAG", "loadStudentsOfClassRoom: ${result.data}")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    studentList = result.data
                                )
                            }
                        }
                    }
                }
        }
    }

    private suspend fun loadSelectedSections(eventId: Int) {
        withContext(dispatcher) {
            sectionRepository.getSelectedSectionOnAssessment(eventId)
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
                                    selectedSectionNameList = result.data.map { section ->
                                        section.name
                                    }
                                )
                            }
                        }
                    }
                }
        }
    }

    private suspend fun loadAssessmentsByEventId(eventId: Int) {
        withContext(dispatcher) {
            assessmentRepository.getAssessmentsByEventId(eventId)
                .catch {
                    _state.update { state -> state.copy(isLoading = false) }
                    _events.send(
                        OnErrorOccurred(
                            errorMessage = it.message ?: application.getString(
                                R.string.unknown_error
                            )
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.update { it.copy(isLoading = true) }

                        is DataResult.Success -> {
                            Log.d("TAG", "loadAssessmentsByEventId: ${state.value}")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    assessmentList = result.data.ifEmpty {
                                        state.value.studentList.map { student ->
                                            Assessment(
                                                id = 0,
                                                studentId = student.id,
                                                eventId = eventId,
                                                score = 0.0,
                                                createdTime = System.currentTimeMillis(),
                                                lastModifiedTime = System.currentTimeMillis()
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
        }
    }

    private suspend fun getStudentAvgScore(studentId: Int): Double {
        return assessmentRepository.getAverageScoreByStudentId(studentId)
            .catch { 0.0 }
            .map { result ->
                when (result) {
                    is DataResult.Success -> result.data
                    else -> 0.0
                }
            }
            .last()
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
        return withContext(dispatcher) {
            categoryRepository.getCategoryById(categoryId)
                .catch { null }
                .map { result ->
                    when (result) {
                        is DataResult.Success -> result.data
                        else -> null
                    }
                }
                .last()
        }
    }

    private suspend fun loadNewAssessment(classRoomId: Int) {
        withContext(dispatcher) {
            loadStudentsOfClassRoom(classRoomId)
            _state.update {
                it.copy(
                    assessmentList = state.value.studentList.map { student ->
                        Assessment(
                            id = 0,
                            studentId = student.id,
                            eventId = 0,
                            score = 0.0,
                            createdTime = System.currentTimeMillis(),
                            lastModifiedTime = System.currentTimeMillis()
                        )
                    }
                )
            }
            val assessmentListField = transformAssessmentToStudentAssessmentState(
                assessmentList = state.value.assessmentList,
                studentList = state.value.studentList
            )
            _state.update {
                it.copy(
                    assessmentListField = assessmentListField
                )
            }
        }
    }

    private suspend fun loadAssessmentDetail(eventId: Int) {
        withContext(dispatcher) {
            eventRepository.getEventById(eventId)
                .catch {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(
                        OnErrorOccurred(
                            application.getString(R.string.event_not_found)
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> _state.update {
                            it.copy(isLoading = true)
                        }

                        is DataResult.Success -> {
                            val assessmentListField = transformAssessmentToStudentAssessmentState(
                                state.value.assessmentList,
                                state.value.studentList
                            )

                            val eventDate = result.data?.eventDate ?: System.currentTimeMillis()

                            val formattedEventDate = SimpleDateFormat(
                                "dd MMMM yyyy",
                                Locale.getDefault()
                            ).format(eventDate)

                            val category = loadCategoryById(
                                categoryId = result.data?.categoryId ?: -1
                            )

                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    assessmentEvent = result.data,
                                    assessmentNameField = TextFieldValue(
                                        text = result.data?.name ?: ""
                                    ),
                                    selectedDate = eventDate,
                                    startDateField = TextFieldValue(
                                        text = formattedEventDate
                                    ),
                                    purposeField = TextFieldValue(
                                        text = result.data?.purpose ?: ""
                                    ),
                                    category = category,
                                    selectedCategoryName = category?.name ?: "",
                                    assessmentListField = assessmentListField
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun saveEvent(assessments: List<StudentAssessmentState>) {
        viewModelScope.launch(dispatcher) {
            Log.d("TAG", "saveEvent: assessments $assessments")
            _state.update { it.copy(assessmentListField = assessments) }

            val event = Event(
                id = state.value.assessmentEvent?.id ?: 0,
                name = state.value.assessmentNameField.text,
                purpose = state.value.purposeField.text,
                eventDate = state.value.selectedDate,
                categoryId = state.value.category?.id ?: 0,
                createdTime = System.currentTimeMillis(),
                lastModifiedTime = System.currentTimeMillis()
            )

            when (val result = eventRepository.upsertEvent(event)) {
                DataResult.Loading -> _state.update { it.copy(isLoading = true) }
                is DataResult.Success -> {
                    Log.d("TAG", "saveEvent: ${result.data}")
                    saveEventSections(result.data?.id ?: state.value.assessmentEvent?.id ?: -1)
                }
            }
        }
    }

    private fun saveEventSections(eventId: Int) {
        viewModelScope.launch(dispatcher) {
            val selectedSectionIds = state.value.selectedSectionNameList.map { sectionName ->
                state.value.sectionList.find { it.name == sectionName }?.id ?: 0
            }

            val eventSections = selectedSectionIds.map { sectionId ->
                EventSection(
                    eventId = eventId,
                    sectionId = sectionId
                )
            }

            when (eventRepository.upsertEventSection(eventSections)) {
                DataResult.Loading -> _state.update { it.copy(isLoading = true) }
                is DataResult.Success -> {
                    saveAssessments(eventId)
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
                        score = assessment.score.text.toDoubleOrNull(),
                        createdTime = System.currentTimeMillis(),
                        lastModifiedTime = System.currentTimeMillis()
                    )
                }
            }

            when (assessmentRepository.upsertAssessments(assessmentList)) {
                DataResult.Loading -> _state.update { it.copy(isLoading = true) }

                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(AssessmentHasSaved)
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

    private fun onCategorySelected(category: String) {
        viewModelScope.launch(dispatcher) {
            val selectedCategory = state.value.categoryList.find { it.name == category }
            _state.update {
                it.copy(
                    category = selectedCategory,
                    selectedCategoryName = category
                )
            }
        }
    }

    private fun checkFormValidity() {
        val isFormValid = state.value.assessmentNameField.text.isNotEmpty() &&
                state.value.selectedCategoryName.isNotEmpty() &&
                state.value.startDateField.text.isNotEmpty() &&
                state.value.selectedSectionNameList.isNotEmpty()

        _state.update { it.copy(isFormValid = isFormValid) }
    }
}