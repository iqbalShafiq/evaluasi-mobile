package id.usecase.assessment.presentation.screens.class_room.detail

import id.usecase.assessment.presentation.models.AlertUi
import id.usecase.assessment.presentation.models.AssessmentUi

data class ClassRoomState(
    val isLoading: Boolean = false,
    val alerts: List<AlertUi> = listOf(),
    val assessments: List<AssessmentUi> = listOf()
)
