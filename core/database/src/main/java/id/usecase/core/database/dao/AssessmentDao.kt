package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import id.usecase.core.database.entities.AssessmentEntity

@Dao
interface AssessmentDao {
    @Transaction
    suspend fun upsertAndGetIds(assessments: List<AssessmentEntity>): List<String> {
        upsert(assessments)
        return assessments.map { it.id }
    }

    @Upsert
    suspend fun upsert(assessments: List<AssessmentEntity>)

    @Query("SELECT * FROM assessments WHERE id IN (:ids)")
    suspend fun getAssessmentsByIds(ids: List<String>): List<AssessmentEntity>

    @Query("SELECT * FROM assessments WHERE event_id = :eventId")
    suspend fun getAssessmentsByEventId(eventId: String): List<AssessmentEntity>

    @Query("SELECT * FROM assessments WHERE id = :id")
    suspend fun getAssessmentById(id: String): AssessmentEntity?

    @Query(
        """
        SELECT AVG(score) 
        FROM assessments 
        JOIN events ON assessments.event_id = events.id
        JOIN categories ON events.category_id = categories.id
        WHERE categories.class_room_id = :classRoomId
        """
    )
    suspend fun getAverageScoreByClassRoomId(classRoomId: String): Double

    @Query(
        """
        SELECT events.name 
        FROM assessments 
        JOIN events ON assessments.event_id = events.id
        JOIN categories ON events.category_id = categories.id
        WHERE categories.class_room_id = :classRoomId
        ORDER BY assessments.created_time DESC
        LIMIT 1
        """
    )
    suspend fun getLastAssessmentByClassRoomId(classRoomId: String): String

    @Query("SELECT AVG(score) FROM assessments WHERE student_id = :studentId")
    suspend fun getAverageScoreByStudentId(studentId: String): Double

    @Delete
    suspend fun delete(assessment: AssessmentEntity)
}