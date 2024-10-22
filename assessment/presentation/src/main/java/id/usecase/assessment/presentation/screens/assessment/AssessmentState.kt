package id.usecase.assessment.presentation.screens.assessment

import androidx.compose.foundation.text.input.TextFieldState
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.core.domain.assessment.model.assessment.category.Category

data class AssessmentState(
    val isLoading: Boolean = false,
    val assessmentName: TextFieldState = TextFieldState(),
    val category: Category? = null,
    val classRoom: ClassRoomUi? = null,
    val students: List<StudentAssessmentState> = listOf(),
)
