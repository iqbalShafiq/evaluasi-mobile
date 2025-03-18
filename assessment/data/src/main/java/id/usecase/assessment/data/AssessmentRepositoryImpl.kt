package id.usecase.assessment.data

import android.util.Log
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.Assessment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AssessmentRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : AssessmentRepository {
    override suspend fun upsertAssessments(assessmentList: List<Assessment>): DataResult<List<Assessment>> {
        return withContext(dispatcher) {
            Log.d("TAG", "upsertAssessments assessment List: $assessmentList")
            val assessmentIds = dataSource.upsertAssessments(assessmentList)
            Log.d("TAG", "upsertAssessments ids: $assessmentIds")
            val assessments = dataSource.getAssessmentsByIds(
                assessmentIds.map {
                    it
                }
            )
            Log.d("TAG", "upsertAssessments upserted: $assessments")

            DataResult.Success(assessments)
        }
    }

    override fun getAssessmentsByEventId(eventId: String): Flow<DataResult<List<Assessment>>> {
        return flow {
            emit(DataResult.Loading)
            val assessments: List<Assessment> = dataSource.getAssessmentsByEventId(eventId)
            emit(DataResult.Success(assessments))
        }.flowOn(dispatcher)
    }

    override fun getAssessmentById(assessmentId: String): Flow<DataResult<Assessment?>> {
        return flow {
            emit(DataResult.Loading)
            val assessment: Assessment = dataSource.getAssessmentById(assessmentId)
                ?: throw Exception("Assessment not found")

            emit(DataResult.Success(assessment))
        }.flowOn(dispatcher)
    }

    override fun getAverageScoreByClassRoomId(classRoomId: String): Flow<DataResult<Double>> {
        return flow {
            emit(DataResult.Loading)
            val avgScore: Double = dataSource.getAverageScoreByClassRoomId(classRoomId)
            emit(DataResult.Success(avgScore))
        }.flowOn(dispatcher)
    }

    override fun getLastAssessmentByClassRoomId(classRoomId: String): Flow<DataResult<String?>> {
        return flow {
            emit(DataResult.Loading)
            val assessment: String? = dataSource.getLastAssessmentByClassRoomId(classRoomId)
            emit(DataResult.Success(assessment))
        }.flowOn(dispatcher)
    }

    override fun getAverageScoreByStudentId(studentId: String): Flow<DataResult<Double>> {
        return flow {
            emit(DataResult.Loading)
            val avgScore: Double = dataSource.getAverageScoreByStudentId(studentId)
            emit(DataResult.Success(avgScore))
        }.flowOn(dispatcher)
    }

    override suspend fun deleteAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            dataSource.deleteAssessment(assessment)
        }
    }

}