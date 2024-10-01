package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.models.Assessment
import id.usecase.core.domain.assessment.models.AssessmentCategory
import id.usecase.core.domain.assessment.models.AssessmentEvent
import id.usecase.core.domain.assessment.models.ClassRoom
import id.usecase.core.domain.assessment.models.Student
import kotlinx.coroutines.flow.Flow

interface AssessmentRepository {
    fun getClassRooms(): Flow<List<ClassRoom>>
    suspend fun addClassRoom(classRoom: ClassRoom)
    suspend fun updateClassRoom(classRoom: ClassRoom)
    suspend fun removeClassRoom(classRoomId: String)

    suspend fun addAssessmentCategoryToClassRoom(category: AssessmentCategory, classRoomId: String)
    suspend fun addAssessmentEventToClassRoom(event: AssessmentEvent, categoryId: String)
    suspend fun addStudentsToClassRoom(studentList: List<Student>, classRoomId: String)
    suspend fun addAssessmentToEvent(assessment: Assessment, eventId: String)

    suspend fun updateAssessmentCategoryFromClassRoom(category: AssessmentCategory)
    suspend fun updateAssessmentEventFromClassRoom(event: AssessmentEvent)
    suspend fun updateStudentFromClassRoom(student: Student, classRoomId: String)
    suspend fun updateAssessment(assessment: Assessment)

    suspend fun removeAssessmentCategoryFromClassRoom(categoryId: String)
    suspend fun removeAssessmentEventFromClassRoom(eventId: String)
    suspend fun removeStudentFromClassRoom(studentId: String)
    suspend fun removeAssessment(assessmentId: String)
}