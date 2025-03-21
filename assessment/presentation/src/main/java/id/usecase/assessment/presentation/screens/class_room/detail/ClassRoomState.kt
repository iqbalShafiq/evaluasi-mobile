package id.usecase.assessment.presentation.screens.class_room.detail

import com.patrykandpatrick.vico.core.entry.ChartEntry
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import id.usecase.core.domain.assessment.model.analytics.LowPerformanceAlert
import id.usecase.core.domain.assessment.model.analytics.SectionScore
import id.usecase.core.domain.assessment.model.analytics.SectionUsage
import id.usecase.core.domain.assessment.model.analytics.StudentProgress
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom

data class ClassRoomState(
    val assessmentEvents: List<AssessmentEventUi> = emptyList(),
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val classRoom: ClassRoom? = null,
    val totalStudents: Int = 0,
    val classAverage: Double = 0.0,
    val categoryList: List<String> = emptyList(),
    val performanceTrendData: List<ChartEntry> = emptyList(),
    val categoryDistributionData: List<ChartEntry> = emptyList(),
    val performanceDistribution: Map<String, Float> = emptyMap(),
    val studentProgress: List<StudentProgress> = emptyList(),
    val categoryAnalysis: List<CategoryAnalysis> = emptyList(),
    val lowPerformanceAlerts: List<LowPerformanceAlert> = emptyList(),
    val sectionUsages: List<SectionUsage> = emptyList(),
    val sectionScores: List<SectionScore> = emptyList()
)
