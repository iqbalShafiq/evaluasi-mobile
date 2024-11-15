package id.usecase.assessment.presentation.screens.assessment

import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState

sealed class AssessmentAction {
    data class LoadAssessmentDetail(val classRoomId: Int, val eventId: Int) : AssessmentAction()

    data class SaveAssessmentEvent(
        val assessments: List<StudentAssessmentState>
    ) : AssessmentAction()

    data class UpdateEventDate(
        val date: String
    ): AssessmentAction()

    data object DeleteAssessmentEvent : AssessmentAction()
}