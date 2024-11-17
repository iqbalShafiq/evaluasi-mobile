package id.usecase.assessment.presentation.screens.class_room.detail

import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom

data class ClassRoomState(
    val isLoading: Boolean = false,
    val assessmentEvents: List<AssessmentEventUi> = emptyList(),
    val events: List<Event> = emptyList(),
    val classRoom: ClassRoom? = null
)
