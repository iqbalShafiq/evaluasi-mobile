package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import id.usecase.core.domain.assessment.model.analytics.LowPerformanceAlert
import id.usecase.core.domain.assessment.model.analytics.SectionScore
import id.usecase.core.domain.assessment.model.analytics.SectionUsage
import id.usecase.core.domain.assessment.model.analytics.StudentProgress
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun getPerformanceTrend(classRoomId: String): Flow<List<Pair<Float, Float>>>
    fun getCategoryDistribution(classRoomId: String): Flow<List<Pair<String, Float>>>
    fun getPerformanceDistribution(classRoomId: String): Flow<Map<String, Float>>
    fun getStudentProgress(classRoomId: String): Flow<List<StudentProgress>>
    fun getCategoryAnalysis(classRoomId: String): Flow<List<CategoryAnalysis>>
    fun getLowPerformanceStudentsByClassRoomId(classRoomId: String): Flow<List<LowPerformanceAlert>>
    fun getSectionScoreDistributionByClassRoomId(classRoomId: String): Flow<List<SectionScore>>
    fun getSectionUsageByClassRoomId(classRoomId: String): Flow<List<SectionUsage>>
}