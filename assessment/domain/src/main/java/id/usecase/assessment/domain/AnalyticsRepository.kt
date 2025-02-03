package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import id.usecase.core.domain.assessment.model.analytics.LowPerformanceAlert
import id.usecase.core.domain.assessment.model.analytics.SectionScore
import id.usecase.core.domain.assessment.model.analytics.SectionUsage
import id.usecase.core.domain.assessment.model.analytics.StudentProgress
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun getPerformanceTrend(classRoomId: Int): Flow<List<Pair<Float, Float>>>
    fun getCategoryDistribution(classRoomId: Int): Flow<List<Pair<String, Float>>>
    fun getPerformanceDistribution(classRoomId: Int): Flow<Map<String, Float>>
    fun getStudentProgress(classRoomId: Int): Flow<List<StudentProgress>>
    fun getCategoryAnalysis(classRoomId: Int): Flow<List<CategoryAnalysis>>
    fun getLowPerformanceStudentsByClassRoomId(classRoomId: Int): Flow<List<LowPerformanceAlert>>
    fun getSectionScoreDistributionByClassRoomId(classRoomId: Int): Flow<List<SectionScore>>
    fun getSectionUsageByClassRoomId(classRoomId: Int): Flow<List<SectionUsage>>
}