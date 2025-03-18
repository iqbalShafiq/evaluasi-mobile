package id.usecase.assessment.domain

import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.model.assessment.Assessment
import kotlinx.coroutines.flow.Flow

interface AssessmentRepository {
    suspend fun upsertAssessments(assessmentList: List<Assessment>): DataResult<List<Assessment>>
    fun getAssessmentsByEventId(eventId: String): Flow<DataResult<List<Assessment>>>
    fun getAssessmentById(assessmentId: String): Flow<DataResult<Assessment?>>
    fun getAverageScoreByClassRoomId(classRoomId: String): Flow<DataResult<Double>>
    fun getLastAssessmentByClassRoomId(classRoomId: String): Flow<DataResult<String?>>
    fun getAverageScoreByStudentId(studentId: String): Flow<DataResult<Double>>
    suspend fun deleteAssessment(assessment: Assessment)
}