package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import id.usecase.core.database.entities.AssessmentEntity

@Dao
interface AssessmentDao {
    @Upsert
    suspend fun upsert(assessments: List<AssessmentEntity>): List<Long>

    @Query("SELECT * FROM assessments WHERE id IN (:ids)")
    suspend fun getAssessmentsByIds(ids: List<Int>): List<AssessmentEntity>

    @Query("SELECT * FROM assessments WHERE event_id = :eventId")
    suspend fun getAssessmentsByEventId(eventId: Int): List<AssessmentEntity>

    @Query("SELECT * FROM assessments WHERE id = :id")
    suspend fun getAssessmentById(id: Int): AssessmentEntity?

    @Query("SELECT AVG(score) FROM assessments WHERE student_id = :studentId")
    suspend fun getAverageScoreByStudentId(studentId: Int): Double

    @Delete
    suspend fun delete(assessment: AssessmentEntity)
}