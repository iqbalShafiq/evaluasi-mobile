package id.usecase.assessment.presentation.screens.assessment.students

import androidx.compose.foundation.text.input.TextFieldState
import id.usecase.assessment.presentation.model.StudentScoreUi

data class StudentAssessmentState(
    val data: StudentScoreUi? = null,
    val score: TextFieldState = TextFieldState(),
    val comments: TextFieldState = TextFieldState(),
)
