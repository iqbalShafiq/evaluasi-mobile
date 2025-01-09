package id.usecase.assessment.presentation.screens.class_room.detail

import com.patrykandpatrick.vico.core.entry.ChartEntry
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom

data class ClassRoomState(
    val assessmentEvents: List<AssessmentEventUi> = emptyList(),
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val classRoom: ClassRoom? = null,
    val performanceTrendData: List<ChartEntry> = emptyList(),
    val categoryDistributionData: List<ChartEntry> = emptyList()
)
