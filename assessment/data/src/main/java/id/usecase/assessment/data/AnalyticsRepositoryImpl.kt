package id.usecase.assessment.data

import id.usecase.assessment.domain.AnalyticsRepository
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.Locale

class AnalyticsRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : AnalyticsRepository {
    override fun getPerformanceTrend(classRoomId: Int): Flow<List<Pair<Float, Float>>> {
        return flow {
            // Get all events in the class room
            val events = dataSource.getEventsByClassRoomId(classRoomId)

            // If there is no event, emit an empty list
            if (events.isEmpty()) {
                emit(emptyList())
                return@flow
            }

            // Get all assessments in the class room
            val assessments = events.flatMap { dataSource.getAssessmentsByEventId(it.id) }

            // Get all months in the class room
            val months = events.map {
                SimpleDateFormat("MM", Locale.getDefault()).format(it.eventDate).toFloat()
            }.distinct()

            // Calculate average score for each month
            val result = months.map { month ->
                val assessmentsInMonth = assessments.filter {
                    SimpleDateFormat("MM", Locale.getDefault()).format(it.lastModifiedTime)
                        .toFloat() == month
                }
                val averageScore = assessmentsInMonth.map { it.score ?: 0.0 }.average().toFloat()
                Pair(month, averageScore)
            }

            // Emit the result
            emit(result)
        }.flowOn(dispatcher)
    }

    override fun getCategoryDistribution(classRoomId: Int): Flow<List<Pair<String, Float>>> {
        return flow {
            // Get all events in the class room
            val events = dataSource.getEventsByClassRoomId(classRoomId)

            // If there is no event, emit an empty list
            if (events.isEmpty()) {
                emit(emptyList())
                return@flow
            }

            // Get all category ids
            val categoryIdList = events
                .map { it.categoryId }
                .distinct()

            // Get all category names
            val categoryNameList = categoryIdList
                .map { dataSource.getCategoryById(categoryId = it)?.name }
                .distinct()

            // Group all assessments by category
            val assessmentsByCategory = categoryIdList
                .associateWith { categoryId ->
                    events
                        .filter { it.categoryId == categoryId }
                        .flatMap { dataSource.getAssessmentsByEventId(it.id) }
                }

            // Calculate average score for each category
            val result = categoryNameList.map { categoryName ->
                val assessments = assessmentsByCategory
                    .filter { it.key == categoryIdList[categoryNameList.indexOf(categoryName)] }
                    .values
                    .flatten()
                val averageScore = assessments.map { it.score ?: 0.0 }.average().toFloat()
                Pair(categoryName ?: "", averageScore)
            }

            // Emit the result
            emit(result)
        }.flowOn(dispatcher)
    }

    override fun getPerformanceDistribution(classRoomId: Int): Flow<Map<String, Float>> {
        return flow {
            // Get all events in the class room
            val events = dataSource.getEventsByClassRoomId(classRoomId)

            // If there is no event, emit an empty map
            if (events.isEmpty()) {
                emit(emptyMap())
                return@flow
            }

            // Get all assessments in the class room
            val assessments = events.flatMap { dataSource.getAssessmentsByEventId(it.id) }

            // Group assessments by performance
            val result = mapOf(
                "Poor" to assessments.filter { (it.score ?: 0.0) < 50.0 },
                "Bad" to assessments.filter {
                    (it.score ?: 0.0) >= 50.0 && (it.score ?: 0.0) < 70.0
                },
                "Good" to assessments.filter {
                    (it.score ?: 0.0) >= 70.0 && (it.score ?: 0.0) < 85.0
                },
                "Great" to assessments.filter { (it.score ?: 0.0) >= 85.0 }
            ).mapValues {
                it.value.map { assessment -> assessment.score ?: 0.0 }.average().toFloat()
            }

            // Emit the result
            emit(result)
        }.flowOn(dispatcher)
    }
}