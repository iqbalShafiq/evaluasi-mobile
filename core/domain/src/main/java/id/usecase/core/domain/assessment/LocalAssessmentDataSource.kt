package id.usecase.core.domain.assessment

import id.usecase.core.domain.assessment.models.Assessment
import id.usecase.core.domain.assessment.models.AssessmentCategory
import id.usecase.core.domain.assessment.models.AssessmentEvent
import id.usecase.core.domain.assessment.models.ClassRoom
import id.usecase.core.domain.assessment.models.Student
import kotlinx.coroutines.flow.Flow

interface LocalAssessmentDataSource {
    suspend fun insertClassRoom(classRooms: ClassRoom)
    suspend fun insertStudentsToClassRoom(classRoomId: String, students: List<Student>)
    suspend fun updateClassRoom(classRoom: ClassRoom)
    suspend fun deleteClassRoom(classRoomId: String)
    suspend fun deleteStudentFromClassRoom(studentId: String)
    fun getClassRooms(): Flow<List<ClassRoom>>
    fun getClassRoomById(classRoomId: String): Flow<ClassRoom?>

    suspend fun insertAssessmentCategory(category: AssessmentCategory, classRoom: ClassRoom)
    suspend fun updateAssessmentCategory(category: AssessmentCategory)
    suspend fun deleteAssessmentCategory(categoryId: String)
    fun getAssessmentCategories(): Flow<List<AssessmentCategory>>
    fun getAssessmentCategoryById(categoryId: String): Flow<AssessmentCategory?>

    suspend fun insertAssessmentEvent(event: AssessmentEvent, category: AssessmentCategory)
    suspend fun updateAssessmentEvent(event: AssessmentEvent)
    suspend fun deleteAssessmentEvent(eventId: String)
    fun getAssessmentEvents(): Flow<List<AssessmentEvent>>
    fun getAssessmentEventById(eventId: String): Flow<AssessmentEvent?>

    suspend fun insertAssessment(assessment: Assessment, event: AssessmentEvent, student: Student)
    suspend fun updateAssessment(assessment: Assessment)
    suspend fun deleteAssessment(assessmentId: String)
    fun getAssessments(): Flow<List<Assessment>>
    fun getAssessmentById(assessmentId: String): Flow<Assessment?>
}