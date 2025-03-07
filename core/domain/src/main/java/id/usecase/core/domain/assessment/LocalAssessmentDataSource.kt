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
    suspend fun getClassRoomById(classRoomId: Int): ClassRoom?
    suspend fun deleteClassRoom(classRoom: ClassRoom)

    suspend fun upsertStudent(students: Student): Student?
    suspend fun upsertStudents(students: List<Student>): List<Student>
    suspend fun getTotalStudent(): Int
    suspend fun getStudentsByClassRoomId(classRoomId: Int): List<Student>
    suspend fun deleteStudent(student: Student)

    suspend fun upsertCategories(categories: List<Category>): List<Long>
    suspend fun upsertCategory(category: Category): Long
    suspend fun getCategoriesByClassRoomId(classRoomId: Int): List<Category>
    suspend fun getCategoryById(categoryId: Int): Category?
    suspend fun getCategoriesByIds(categoryIds: List<Int>): List<Category>
    suspend fun deleteCategory(category: Category)

    suspend fun upsertEvent(event: Event): Long
    suspend fun upsertEventSection(eventSections: List<EventSection>): List<Long>
    suspend fun getEventsByClassRoomId(classRoomId: Int): List<Event>
    suspend fun getEventsByCategoryId(categoryId: Int): List<Event>
    suspend fun getEventById(eventId: Int): Event?
    suspend fun deleteEvent(event: Event)

    suspend fun upsertAssessments(assessmentList: List<Assessment>): List<Long>
    suspend fun getAssessmentsByIds(assessmentIds: List<Int>): List<Assessment>
    suspend fun getAssessmentsByEventId(eventId: Int): List<Assessment>
    suspend fun getAssessmentById(assessmentId: Int): Assessment?
    suspend fun getAverageScoreByClassRoomId(classRoomId: Int): Double
    suspend fun getLastAssessmentByClassRoomId(classRoomId: Int): String?
    suspend fun getAverageScoreByStudentId(studentId: Int): Double
    suspend fun deleteAssessment(assessment: Assessment)

    suspend fun getPerformanceTrendByClassRoom(classRoomId: Int): List<MonthlyScore>
    suspend fun getCategoryDistributionByClassRoom(classRoomId: Int): List<CategoryScore>
    suspend fun getPerformanceDistributionByClassRoom(classRoomId: Int): List<PerformanceScore>
    suspend fun getStudentProgressByClassRoom(classRoomId: Int): List<StudentProgress>
    suspend fun getCategoryAnalysisByClassRoom(classRoomId: Int): List<CategoryAnalysis>
    suspend fun getLowPerformanceStudentsByClassRoomId(classRoomId: Int): List<LowPerformanceAlert>
    suspend fun getSectionScoreDistributionByClassRoomId(classRoomId: Int): List<SectionScore>
    suspend fun getSectionUsageByClassRoomId(classRoomId: Int): List<SectionUsage>

    suspend fun upsertSection(section: List<Section>): List<Long>
    suspend fun getSectionById(sectionId: Int): Section?
    suspend fun getSectionsByClassRoomId(classRoomId: Int): List<Section>
    suspend fun getSelectedSectionOnAssessment(assessmentId: Int): List<Section>
}