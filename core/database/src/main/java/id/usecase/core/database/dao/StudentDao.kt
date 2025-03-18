package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import id.usecase.core.database.entities.StudentEntity

@Dao
interface StudentDao {

    @Transaction
    suspend fun upsertAndGetId(student: StudentEntity): String {
        upsert(student)
        return student.id
    }

    @Transaction
    suspend fun upsertAndGetId(studentList: List<StudentEntity>): List<String> {
        upsert(studentList)
        return studentList.map { it.id }
    }

    @Upsert
    suspend fun upsert(student: StudentEntity)

    @Upsert
    suspend fun upsert(studentList: List<StudentEntity>)

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: String): StudentEntity?

    @Query("SELECT COUNT(*) FROM students")
    suspend fun getTotalStudent(): Int

    @Query("SELECT * FROM students WHERE class_room_id = :classRoomId")
    suspend fun getStudentsByClassRoomId(classRoomId: String): List<StudentEntity>

    @Query(
        """
        SELECT students.* FROM students
        INNER JOIN assessments ON students.id = assessments.student_id
        WHERE assessments.event_id = :eventId
        """
    )
    suspend fun getStudentsByEventId(eventId: String): List<StudentEntity>

    @Delete
    suspend fun delete(student: StudentEntity)
}