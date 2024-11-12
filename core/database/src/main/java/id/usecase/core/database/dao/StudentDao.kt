package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import id.usecase.core.database.entities.StudentEntity

@Dao
interface StudentDao {
    @Upsert
    suspend fun upsert(student: StudentEntity): Long

    @Upsert
    suspend fun upsert(studentList: List<StudentEntity>): List<Long>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): StudentEntity?

    @Query("SELECT * FROM students WHERE class_room_id = :classRoomId")
    suspend fun getStudentsByClassRoomId(classRoomId: Int): List<StudentEntity>

    @Query(
        """
        SELECT students.* FROM students
        INNER JOIN assessments ON students.id = assessments.student_id
        WHERE assessments.event_id = :eventId
        """
    )
    suspend fun getStudentsByEventId(eventId: Int): List<StudentEntity>

    @Delete
    suspend fun delete(student: StudentEntity)
}