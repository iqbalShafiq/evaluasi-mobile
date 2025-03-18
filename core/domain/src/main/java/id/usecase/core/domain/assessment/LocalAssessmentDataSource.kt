package id.usecase.core.domain.assessment

import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import id.usecase.core.domain.assessment.model.analytics.CategoryScore
import id.usecase.core.domain.assessment.model.analytics.LowPerformanceAlert
import id.usecase.core.domain.assessment.model.analytics.MonthlyScore
import id.usecase.core.domain.assessment.model.analytics.PerformanceScore
import id.usecase.core.domain.assessment.model.analytics.SectionScore
import id.usecase.core.domain.assessment.model.analytics.SectionUsage
import id.usecase.core.domain.assessment.model.analytics.StudentProgress
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.section.EventSection
import id.usecase.core.domain.assessment.model.section.Section
import id.usecase.core.domain.assessment.model.student.Student

interface LocalAssessmentDataSource {
    suspend fun upsertClassRoom(classRoom: ClassRoom): ClassRoom?
    suspend fun getClassRooms(): List<ClassRoom>
    suspend fun searchClassRooms(query: String): List<ClassRoom>
    suspend fun getClassRoomById(classRoomId: String): ClassRoom?
    suspend fun deleteClassRoom(classRoom: ClassRoom)

    suspend fun upsertStudent(students: Student): Student?
    suspend fun upsertStudents(students: List<Student>): List<Student>
    suspend fun getStudentById(studentId: String): Student?
    suspend fun getTotalStudent(): Int
    suspend fun getStudentsByClassRoomId(classRoomId: String): List<Student>
    suspend fun deleteStudent(student: Student)

    suspend fun upsertCategories(categories: List<Category>): List<String>
    suspend fun upsertCategory(category: Category): String
    suspend fun getCategoriesByClassRoomId(classRoomId: String): List<Category>
    suspend fun getCategoryById(categoryId: String): Category?
    suspend fun getCategoriesByIds(categoryIds: List<String>): List<Category>
    suspend fun deleteCategory(category: Category)

    suspend fun upsertEvent(event: Event): String
    suspend fun getEventsByClassRoomId(classRoomId: String): List<Event>
    suspend fun getEventsByCategoryId(categoryId: String): List<Event>
    suspend fun getEventById(eventId: String): Event?
    suspend fun deleteEvent(event: Event)

    suspend fun upsertEventSection(eventSections: List<EventSection>): List<String>
    suspend fun getEventSectionCrossRef(eventSectionId: String): EventSection?
    suspend fun getEventSectionCrossRef(eventSectionIdList: List<String>): List<EventSection>

    suspend fun upsertAssessments(assessmentList: List<Assessment>): List<String>
    suspend fun getAssessmentsByIds(assessmentIds: List<String>): List<Assessment>
    suspend fun getAssessmentsByEventId(eventId: String): List<Assessment>
    suspend fun getAssessmentById(assessmentId: String): Assessment?
    suspend fun getAverageScoreByClassRoomId(classRoomId: String): Double
    suspend fun getLastAssessmentByClassRoomId(classRoomId: String): String?
    suspend fun getAverageScoreByStudentId(studentId: String): Double
    suspend fun deleteAssessment(assessment: Assessment)

    suspend fun getPerformanceTrendByClassRoom(classRoomId: String): List<MonthlyScore>
    suspend fun getCategoryDistributionByClassRoom(classRoomId: String): List<CategoryScore>
    suspend fun getPerformanceDistributionByClassRoom(classRoomId: String): List<PerformanceScore>
    suspend fun getStudentProgressByClassRoom(classRoomId: String): List<StudentProgress>
    suspend fun getCategoryAnalysisByClassRoom(classRoomId: String): List<CategoryAnalysis>
    suspend fun getLowPerformanceStudentsByClassRoomId(classRoomId: String): List<LowPerformanceAlert>
    suspend fun getSectionScoreDistributionByClassRoomId(classRoomId: String): List<SectionScore>
    suspend fun getSectionUsageByClassRoomId(classRoomId: String): List<SectionUsage>

    suspend fun upsertSection(section: List<Section>): List<String>
    suspend fun getSectionById(sectionId: String): Section?
    suspend fun getSectionByIds(sectionIds: List<String>): List<Section>
    suspend fun getSectionsByClassRoomId(classRoomId: String): List<Section>
    suspend fun getSelectedSectionOnAssessment(assessmentId: String): List<Section>
}