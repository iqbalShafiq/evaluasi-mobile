package id.usecase.assessment.presentation.screens.assessment

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.text.input.TextFieldValue
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.student.Student

data class AssessmentState(
    val isLoading: Boolean = false,
    val assessmentNameField: TextFieldValue = TextFieldValue(),
    val startDateField: TextFieldValue = TextFieldValue(),
    val categoryField: TextFieldValue = TextFieldValue(),
    val assessmentListField: List<StudentAssessmentState> = emptyList(),
    val assessmentEvent: Event? = null,
    val category: Category? = null,
    val classRoom: ClassRoomUi? = null,
    val assessmentList: List<Assessment> = emptyList(),
    val studentList: List<Student> = emptyList(),
)
