package id.usecase.assessment.presentation.screens.assessment

import id.usecase.core.domain.assessment.model.assessment.event.Event

sealed class AssessmentAction {
    data class LoadAssessmentDetail(val classRoomId: Int, val eventId: Int) : AssessmentAction()
    data class SaveAssessmentEvent(val assessmentEvent: Event) : AssessmentAction()
    data class DeleteAssessmentEvent(val eventId: Int) : AssessmentAction()
}