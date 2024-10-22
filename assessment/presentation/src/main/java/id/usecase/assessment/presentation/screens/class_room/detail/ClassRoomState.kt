package id.usecase.assessment.presentation.screens.class_room.detail

import id.usecase.assessment.presentation.model.AlertUi
import id.usecase.assessment.presentation.model.AssessmentEventUi

data class ClassRoomState(
    val isLoading: Boolean = false,
    val alerts: List<AlertUi> = listOf(),
    val assessmentEvents: List<AssessmentEventUi> = listOf()
)
