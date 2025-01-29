package id.usecase.assessment.data

import id.usecase.assessment.domain.AnalyticsRepository
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import id.usecase.core.domain.assessment.model.analytics.StudentProgress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AnalyticsRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : AnalyticsRepository {
    override fun getPerformanceTrend(classRoomId: Int): Flow<List<Pair<Float, Float>>> {
        return flow {
            val result = dataSource.getPerformanceTrendByClassRoom(classRoomId)
                .map { Pair(it.month, it.averageScore) }
            emit(result)
        }.flowOn(dispatcher)
    }

    override fun getCategoryDistribution(classRoomId: Int): Flow<List<Pair<String, Float>>> {
        return flow {
            val result = dataSource.getCategoryDistributionByClassRoom(classRoomId)
                .map { Pair(it.categoryName, it.totalAssessments) }
            emit(result)
        }.flowOn(dispatcher)
    }

    override fun getPerformanceDistribution(classRoomId: Int): Flow<Map<String, Float>> {
        return flow {
            val result = dataSource.getPerformanceDistributionByClassRoom(classRoomId)
                .associate { it.performanceLevel to it.averageScore }
            emit(result)
        }.flowOn(dispatcher)
    }

    override fun getStudentProgress(classRoomId: Int): Flow<List<StudentProgress>> {
        return flow {
            val result = dataSource.getStudentProgressByClassRoom(classRoomId)
                .map { StudentProgress(it.studentName, it.progressPercentage, it.lastUpdated) }
            emit(result)
        }.flowOn(dispatcher)
    }

    override fun getCategoryAnalysis(classRoomId: Int): Flow<List<CategoryAnalysis>> {
        return flow {
            val result = dataSource.getCategoryAnalysisByClassRoom(classRoomId)
                .map { CategoryAnalysis(it.categoryName, it.averageScore) }
            emit(result)
        }.flowOn(dispatcher)
    }
}