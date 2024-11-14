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
    override suspend fun upsertAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            try {
                dataSource.upsertAssessment(assessment)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getAssessmentsByEventId(eventId: Int): Flow<DataResult<List<Assessment>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val assessments: List<Assessment> =
                    dataSource.getAssessmentsByEventId(eventId)
                if (assessments.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any assessment yet")
                        )
                    )
                    return@flow
                }
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