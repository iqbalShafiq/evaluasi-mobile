package id.usecase.assessment.data

import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.core.domain.assessment.models.Assessment
import id.usecase.core.domain.assessment.models.AssessmentCategory
import id.usecase.core.domain.assessment.models.AssessmentEvent
import id.usecase.core.domain.assessment.models.ClassRoom
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.models.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class AssessmentRepositoryImpl(
    private val assessmentDataSource: LocalAssessmentDataSource
) : AssessmentRepository {
    override fun getClassRooms(): Flow<List<ClassRoom>> {
        return assessmentDataSource.getClassRooms()
    }

    override suspend fun addClassRoom(classRoom: ClassRoom) {
        assessmentDataSource.insertClassRoom(classRoom)
    }

    override suspend fun updateClassRoom(classRoom: ClassRoom) {
        assessmentDataSource.updateClassRoom(classRoom)
    }

    override suspend fun removeClassRoom(classRoomId: String) {
        assessmentDataSource.deleteClassRoom(classRoomId)
    }

    override suspend fun addAssessmentCategoryToClassRoom(
        category: AssessmentCategory,
        classRoomId: String
    ) {
        val classRoom = assessmentDataSource.getClassRoomById(classRoomId).firstOrNull()
        if (classRoom != null) assessmentDataSource.insertAssessmentCategory(
            category = category,
            classRoom = classRoom
        )
        else throw IllegalArgumentException("Class room is not found")
    }

    override suspend fun addStudentsToClassRoom(studentList: List<Student>, classRoomId: String) {
        val classRoom = assessmentDataSource.getClassRoomById(classRoomId).firstOrNull()
        if (classRoom != null) assessmentDataSource.insertStudentsToClassRoom(
            classRoomId = classRoomId,
            students = studentList
        )
        else throw IllegalArgumentException("Class room is not found")
    }

    override suspend fun addAssessmentToEvent(assessment: Assessment, eventId: String) {
        val event = assessmentDataSource.getAssessmentEventById(eventId).firstOrNull()

        if (event != null) {
            assessment.student?.let { student ->
                assessmentDataSource.insertAssessment(
                    assessment = assessment,
                    event = event,
                    student = student
                )
            } ?: run { throw IllegalArgumentException("Student is required") }


        } else throw IllegalArgumentException("Event is not found")
    }

    override suspend fun addAssessmentEventToClassRoom(
        event: AssessmentEvent,
        categoryId: String
    ) {
        val category = assessmentDataSource.getAssessmentCategoryById(categoryId).firstOrNull()
        if (category != null) assessmentDataSource.insertAssessmentEvent(event, category)
        else throw IllegalArgumentException("Category is not found")
    }

    override suspend fun updateAssessmentCategoryFromClassRoom(category: AssessmentCategory) {
        assessmentDataSource.updateAssessmentCategory(category)
    }

    override suspend fun updateAssessmentEventFromClassRoom(event: AssessmentEvent) {
        assessmentDataSource.updateAssessmentEvent(event)
    }

    override suspend fun updateStudentFromClassRoom(student: Student, classRoomId: String) {
        val classRoom = assessmentDataSource.getClassRoomById(classRoomId).firstOrNull()
        if (classRoom != null) assessmentDataSource.insertStudentsToClassRoom(
            classRoomId = classRoomId,
            students = listOf(student)
        )
        else throw IllegalArgumentException("Class room is not found")
    }

    override suspend fun updateAssessment(assessment: Assessment) {
        assessmentDataSource.updateAssessment(assessment)
    }

    override suspend fun removeAssessmentCategoryFromClassRoom(categoryId: String) {
        assessmentDataSource.deleteAssessmentCategory(categoryId)
    }

    override suspend fun removeAssessmentEventFromClassRoom(eventId: String) {
        assessmentDataSource.deleteAssessmentEvent(eventId)
    }

    override suspend fun removeStudentFromClassRoom(studentId: String) {
        assessmentDataSource.deleteStudentFromClassRoom(studentId)
    }

    override suspend fun removeAssessment(assessmentId: String) {
        assessmentDataSource.deleteAssessment(assessmentId)
    }
}