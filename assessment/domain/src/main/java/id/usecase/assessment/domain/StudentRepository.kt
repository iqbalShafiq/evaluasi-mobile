package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.student.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    suspend fun upsertStudent(students: Student): DataResult<Student?>
    suspend fun upsertStudents(students: List<Student>): DataResult<List<Student>>
    fun getStudentsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Student>>>
    suspend fun deleteStudent(student: Student)
}