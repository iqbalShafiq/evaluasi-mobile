package id.usecase.core.domain.assessment

import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.student.Student

interface LocalAssessmentDataSource {
    suspend fun upsertClassRoom(classRoom: ClassRoom): ClassRoom?
    suspend fun getClassRooms(): List<ClassRoom>
    suspend fun getClassRoomById(classRoomId: Int): ClassRoom?
    suspend fun deleteClassRoom(classRoom: ClassRoom)

    suspend fun insertStudent(students: Student)
    suspend fun insertStudents(students: List<Student>)
    suspend fun getStudentsByClassRoomId(classRoomId: Int): List<Student>
    suspend fun deleteStudent(student: Student)

    suspend fun upsertCategory(category: Category)
    suspend fun getCategoriesByClassRoomId(classRoomId: Int): List<Category>
    suspend fun getCategoryById(categoryId: Int): Category?
    suspend fun deleteCategory(category: Category)

    suspend fun upsertEvent(event: Event)
    suspend fun getEventsByClassRoomId(classRoomId: Int): List<Event>
    suspend fun getEventsByCategoryId(categoryId: Int): List<Event>
    suspend fun getEventById(eventId: Int): Event?
    suspend fun deleteEvent(event: Event)

    suspend fun upsertAssessment(assessment: Assessment)
    suspend fun getAssessmentsByEventId(eventId: Int): List<Assessment>
    suspend fun getAssessmentById(assessmentId: Int): Assessment?
    suspend fun deleteAssessment(assessment: Assessment)
}