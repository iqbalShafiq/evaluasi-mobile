package id.usecase.assessment.presentation.screens.assessment.students

import androidx.compose.ui.text.input.TextFieldValue
import id.usecase.assessment.presentation.model.StudentScoreUi

data class StudentAssessmentState(
    val data: StudentScoreUi? = null,
    val score: TextFieldValue = TextFieldValue(),
    val comments: TextFieldValue = TextFieldValue(),
)
