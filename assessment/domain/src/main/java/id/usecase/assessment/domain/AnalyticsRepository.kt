package id.usecase.assessment.domain

import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun getPerformanceTrend(classRoomId: Int): Flow<List<Pair<Float, Float>>>
    fun getCategoryDistribution(classRoomId: Int): Flow<List<Pair<String, Float>>>
    fun getPerformanceDistribution(classRoomId: Int): Flow<Map<String, Float>>
}