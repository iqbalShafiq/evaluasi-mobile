package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.assessment.Assessment
import kotlinx.coroutines.flow.Flow

interface AssessmentRepository {
    suspend fun upsertAssessment(assessment: Assessment)
    fun getAssessmentsByEventId(eventId: Int): Flow<DataResult<List<Assessment>>>
    fun getAssessmentById(assessmentId: Int): Flow<DataResult<Assessment?>>
    suspend fun deleteAssessment(assessment: Assessment)
}