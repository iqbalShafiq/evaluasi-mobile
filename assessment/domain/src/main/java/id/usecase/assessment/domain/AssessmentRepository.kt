package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.utils.DataResult
import id.usecase.core.domain.assessment.model.assessment.Assessment
import kotlinx.coroutines.flow.Flow

interface AssessmentRepository {
    suspend fun upsertAssessments(assessmentList: List<Assessment>): DataResult<List<Assessment>>
    fun getAssessmentsByEventId(eventId: Int): Flow<DataResult<List<Assessment>>>
    fun getAssessmentById(assessmentId: Int): Flow<DataResult<Assessment?>>
    fun getAverageScoreByClassRoomId(classRoomId: Int): Flow<DataResult<Double>>
    fun getLastAssessmentByClassRoomId(classRoomId: Int): Flow<DataResult<String?>>
    fun getAverageScoreByStudentId(studentId: Int): Flow<DataResult<Double>>
    suspend fun deleteAssessment(assessment: Assessment)
}