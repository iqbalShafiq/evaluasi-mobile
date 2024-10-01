package id.usecase.assessment.presentation.screens.assessment

import androidx.compose.foundation.text.input.TextFieldState
import id.usecase.assessment.presentation.models.ClassRoomUi
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.core.domain.assessment.models.AssessmentCategory
import id.usecase.core.domain.assessment.models.AssessmentEvent

data class AssessmentState(
    val isLoading: Boolean = false,
    val assessmentName: TextFieldState = TextFieldState(),
    val category: AssessmentCategory? = null,
    val classRoom: ClassRoomUi? = null,
    val students: List<StudentAssessmentState> = listOf(),
)
