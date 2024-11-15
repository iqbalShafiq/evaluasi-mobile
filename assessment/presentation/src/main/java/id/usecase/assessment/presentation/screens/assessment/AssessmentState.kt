package id.usecase.assessment.presentation.screens.assessment

import androidx.compose.foundation.text.input.TextFieldState
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.student.Student

data class AssessmentState(
    val isLoading: Boolean = false,
    val assessmentNameField: TextFieldState = TextFieldState(),
    val startDateField: TextFieldState = TextFieldState(),
    val categoryField: TextFieldState = TextFieldState(),
    val assessmentListField: List<StudentAssessmentState> = emptyList(),
    val category: Category? = null,
    val classRoom: ClassRoomUi? = null,
    val assessmentList: List<Assessment> = emptyList(),
    val studentList: List<Student> = emptyList(),
)
