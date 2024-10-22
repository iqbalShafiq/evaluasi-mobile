package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.student.Student
import kotlinx.coroutines.flow.Flow

interface AssessmentRepository {
    suspend fun upsertClassRoom(classRoom: ClassRoom)
    fun getClassRooms(): Flow<DataResult<List<ClassRoom>>>
    fun getClassRoomById(classRoomId: Int): Flow<DataResult<ClassRoom?>>
    suspend fun deleteClassRoom(classRoom: ClassRoom)

    suspend fun insertStudent(students: Student)
    suspend fun insertStudents(students: List<Student>)
    fun getStudentsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Student>>>
    suspend fun deleteStudent(student: Student)

    suspend fun upsertCategory(category: Category)
    fun getCategoriesByClassRoomId(classRoomId: Int): Flow<DataResult<List<Category>>>
    fun getCategoryById(categoryId: Int): Flow<DataResult<Category?>>
    suspend fun deleteCategory(category: Category)

    suspend fun upsertEvent(event: Event)
    fun getEventsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Event>>>
    fun getEventsByCategoryId(categoryId: Int): Flow<DataResult<List<Event>>>
    fun getEventById(eventId: Int): Flow<DataResult<Event?>>
    suspend fun deleteEvent(event: Event)

    suspend fun upsertAssessment(assessment: Assessment)
    fun getAssessmentsByEventId(eventId: Int): Flow<DataResult<List<Assessment>>>
    fun getAssessmentById(assessmentId: Int): Flow<DataResult<Assessment?>>
    suspend fun deleteAssessment(assessment: Assessment)
}