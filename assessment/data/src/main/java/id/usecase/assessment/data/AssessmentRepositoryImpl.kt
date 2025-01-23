package id.usecase.assessment.data

import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.core.domain.assessment.DataResult
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
            try {
                val assessmentIds = dataSource.upsertAssessments(assessmentList)
                val assessments = dataSource.getAssessmentsByIds(
                    assessmentIds.map {
                        it.toInt()
                    }
                )

                DataResult.Success(assessments)
            } catch (e: Exception) {
                DataResult.Error(e)
            }
        }
    }

    override fun getAssessmentsByEventId(eventId: Int): Flow<DataResult<List<Assessment>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val assessments: List<Assessment> = dataSource.getAssessmentsByEventId(eventId)
                emit(DataResult.Success(assessments))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getAssessmentById(assessmentId: Int): Flow<DataResult<Assessment?>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val assessment: Assessment? = dataSource.getAssessmentById(assessmentId)
                if (assessment == null) {
                    emit(
                        DataResult.Error(
                            Exception("Assessment not found")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(assessment))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getAverageScoreByStudentId(studentId: Int): Flow<DataResult<Double>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val avgScore: Double = dataSource.getAverageScoreByStudentId(studentId)
                emit(DataResult.Success(avgScore))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            try {
                dataSource.deleteAssessment(assessment)
            } catch (e: Exception) {
                throw e
            }
        }
    }

}