package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import id.usecase.core.database.entities.AssessmentEntity

@Dao
interface AssessmentDao {
    @Upsert
    suspend fun upsert(assessment: AssessmentEntity)

    @Query("SELECT * FROM assessments WHERE event_id = :eventId")
    suspend fun getAssessmentsByEventId(eventId: Int): List<AssessmentEntity>

    @Query("SELECT * FROM assessments WHERE id = :id")
    suspend fun getAssessmentById(id: Int): AssessmentEntity?

    @Delete
    suspend fun delete(assessment: AssessmentEntity)
}