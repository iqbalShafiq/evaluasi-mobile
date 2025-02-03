package id.usecase.core.database

import id.usecase.core.database.dao.AnalyticsDao
import id.usecase.core.database.dao.AssessmentDao
import id.usecase.core.database.dao.CategoryDao
import id.usecase.core.database.dao.ClassRoomDao
import id.usecase.core.database.dao.EventDao
import id.usecase.core.database.dao.SectionDao
import id.usecase.core.database.dao.StudentDao
import id.usecase.core.database.entities.EventSectionCrossRef
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomLocalAssessmentDataSource(
    private val classRoomDao: ClassRoomDao,
    private val categoryDao: CategoryDao,
    private val eventDao: EventDao,
    private val assessmentDao: AssessmentDao,
    private val studentDao: StudentDao,
    private val dispatcher: CoroutineDispatcher,
    private val analyticsDao: AnalyticsDao,
    private val sectionDao: SectionDao
) : LocalAssessmentDataSource {
    override suspend fun upsertClassRoom(classRoom: ClassRoom): ClassRoom? {
        return withContext(dispatcher) {
            val id = classRoomDao.upsert(classRoom.toEntity())
            return@withContext classRoomDao.getClassRoomById(id.toInt())?.toDomainForm()
        }
    }

    override suspend fun getClassRooms(): List<ClassRoom> {
        return withContext(dispatcher) {
            classRoomDao
                .getAllClassRooms()
                .map { it.toDomainForm() }
        }
    }

    override suspend fun searchClassRooms(query: String): List<ClassRoom> {
        return withContext(dispatcher) {
            classRoomDao
                .searchClassRooms(query)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun getClassRoomById(classRoomId: Int): ClassRoom? {
        return withContext(dispatcher) {
            classRoomDao
                .getClassRoomById(classRoomId)
                ?.toDomainForm()
        }
    }

    override suspend fun deleteClassRoom(classRoom: ClassRoom) {
        withContext(dispatcher) {
            classRoomDao.delete(classRoom.toEntity())
        }
    }

    override suspend fun upsertStudent(students: Student): Student? {
        return withContext(dispatcher) {
            val id = studentDao.upsert(students.toEntity())
            studentDao
                .getStudentById(id.toInt())
                ?.toDomainForm()
        }
    }

    override suspend fun upsertStudents(students: List<Student>): List<Student> {
        return withContext(dispatcher) {
            val studentEntities = students.map {
                it.toEntity()
            }

            val ids = studentDao.upsert(
                studentList = studentEntities
            )

            studentDao
                .getStudentsByClassRoomId(ids.first().toInt())
                .map { it.toDomainForm() }
        }
    }

    override suspend fun getTotalStudent(): Int {
        return withContext(dispatcher) {
            studentDao.getTotalStudent()
        }
    }

    override suspend fun getStudentsByClassRoomId(classRoomId: Int): List<Student> {
        return withContext(dispatcher) {
            studentDao
                .getStudentsByClassRoomId(classRoomId)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun deleteStudent(student: Student) {
        withContext(dispatcher) {
            studentDao.delete(student.toEntity())
        }
    }

    override suspend fun upsertCategories(categories: List<Category>): List<Long> {
        return withContext(dispatcher) {
            val categoryEntities = categories.map {
                it.toEntity()
            }

            categoryDao.upsert(
                categories = categoryEntities
            )
        }
    }

    override suspend fun upsertCategory(category: Category): Long {
        return withContext(dispatcher) {
            categoryDao.upsert(category.toEntity())
        }
    }

    override suspend fun getCategoriesByClassRoomId(classRoomId: Int): List<Category> {
        return withContext(dispatcher) {
            categoryDao
                .getCategoriesByClassRoomId(classRoomId)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun getCategoryById(categoryId: Int): Category? {
        return withContext(dispatcher) {
            categoryDao
                .getCategoryById(categoryId)
                ?.toDomainForm()
        }
    }

    override suspend fun getCategoriesByIds(categoryIds: List<Int>): List<Category> {
        return withContext(dispatcher) {
            categoryDao
                .getCategoriesByIds(categoryIds)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun deleteCategory(category: Category) {
        withContext(dispatcher) {
            categoryDao.delete(category.toEntity())
        }
    }

    override suspend fun upsertEvent(event: Event): Long {
        return withContext(dispatcher) {
            eventDao.upsert(event.toEntity())
        }
    }

    override suspend fun upsertEventSection(eventSections: List<EventSection>): List<Long> {
        return withContext(dispatcher) {
            val crossRef = withContext(Dispatchers.Default) {
                eventSections.map {
                    EventSectionCrossRef(
                        eventId = it.eventId,
                        sectionId = it.sectionId
                    )
                }
            }

            val unselectedSections = withContext(Dispatchers.Default) {
                sectionDao.getSelectedSectionOnAssessment(eventSections.first().eventId)
                    .filter { section -> eventSections.none { it.sectionId == section.id } }
                    .map { EventSectionCrossRef(eventSections.first().eventId, it.id) }
            }

            eventDao.deleteEventSection(unselectedSections)
            eventDao.upsertEventSection(crossRef)
        }
    }

    override suspend fun getEventsByClassRoomId(classRoomId: Int): List<Event> {
        return withContext(dispatcher) {
            eventDao
                .getEventsByClassRoomId(classRoomId)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun getEventsByCategoryId(categoryId: Int): List<Event> {
        return withContext(dispatcher) {
            eventDao
                .getEventsByCategoryId(categoryId)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun getEventById(eventId: Int): Event? {
        return withContext(dispatcher) {
            eventDao
                .getEventById(eventId)
                ?.toDomainForm()
        }
    }

    override suspend fun deleteEvent(event: Event) {
        withContext(dispatcher) {
            eventDao.delete(event.toEntity())
        }
    }

    override suspend fun upsertAssessments(assessmentList: List<Assessment>): List<Long> {
        return withContext(dispatcher) {
            val mappedAssessmentList = withContext(Dispatchers.Default) {
                assessmentList.map {
                    it.toEntity()
                }
            }

            assessmentDao.upsert(mappedAssessmentList)
        }
    }

    override suspend fun getAssessmentsByIds(assessmentIds: List<Int>): List<Assessment> {
        return withContext(dispatcher) {
            assessmentDao
                .getAssessmentsByIds(assessmentIds)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun getAssessmentsByEventId(eventId: Int): List<Assessment> {
        return withContext(dispatcher) {
            assessmentDao
                .getAssessmentsByEventId(eventId)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun getAssessmentById(assessmentId: Int): Assessment? {
        return withContext(dispatcher) {
            assessmentDao
                .getAssessmentById(assessmentId)
                ?.toDomainForm()
        }
    }

    override suspend fun getAverageScoreByClassRoomId(classRoomId: Int): Double {
        return withContext(dispatcher) {
            assessmentDao.getAverageScoreByClassRoomId(classRoomId)
        }
    }

    override suspend fun getLastAssessmentByClassRoomId(classRoomId: Int): String {
        return withContext(dispatcher) {
            assessmentDao.getLastAssessmentByClassRoomId(classRoomId)
        }
    }

    override suspend fun getAverageScoreByStudentId(studentId: Int): Double {
        return withContext(dispatcher) {
            assessmentDao.getAverageScoreByStudentId(studentId)
        }
    }

    override suspend fun deleteAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            assessmentDao.delete(assessment.toEntity())
        }
    }

    override suspend fun getPerformanceTrendByClassRoom(classRoomId: Int): List<MonthlyScore> {
        return withContext(dispatcher) {
            val result = analyticsDao.getPerformanceTrendByClassRoom(classRoomId)
            result.map { it.toDomainForm() }
        }
    }

    override suspend fun getCategoryDistributionByClassRoom(classRoomId: Int): List<CategoryScore> {
        return withContext(dispatcher) {
            val result = analyticsDao.getCategoryDistributionByClassRoom(classRoomId)
            result.map { it.toDomainForm() }
        }
    }

    override suspend fun getPerformanceDistributionByClassRoom(classRoomId: Int): List<PerformanceScore> {
        return withContext(dispatcher) {
            val result = analyticsDao.getPerformanceDistributionByClassRoom(classRoomId)
            result.map { it.toDomainForm() }
        }
    }

    override suspend fun getStudentProgressByClassRoom(classRoomId: Int): List<StudentProgress> {
        return withContext(dispatcher) {
            val result = analyticsDao.getStudentProgressByClassRoom(classRoomId)
            result.map { it.toDomainForm() }
        }
    }

    override suspend fun getCategoryAnalysisByClassRoom(classRoomId: Int): List<CategoryAnalysis> {
        return withContext(dispatcher) {
            val result = analyticsDao.getCategoryAnalysisByClassRoom(classRoomId)
            result.map { it.toDomainForm() }
        }
    }

    override suspend fun getLowPerformanceStudentsByClassRoomId(classRoomId: Int): List<LowPerformanceAlert> {
        return withContext(dispatcher) {
            val result = analyticsDao.getLowPerformanceStudentsByClassRoomId(classRoomId)
            result.map { it.toDomainForm() }
        }
    }

    override suspend fun getSectionUsageByClassRoomId(classRoomId: Int): List<SectionUsage> {
        return withContext(dispatcher) {
            val result = analyticsDao.getSectionUsageByClassRoomId(classRoomId)
            result.map { it.toDomainForm() }
        }
    }

    override suspend fun getSectionScoreDistributionByClassRoomId(classRoomId: Int): List<SectionScore> {
        return withContext(dispatcher) {
            val result = analyticsDao.getSectionScoreDistributionByClassRoomId(classRoomId)
            result.map { it.toDomainForm() }
        }
    }

    override suspend fun upsertSection(section: List<Section>): List<Long> {
        return withContext(dispatcher) {
            val sectionEntities = section.map {
                it.toEntity()
            }

            sectionDao.upsert(sectionEntities)
        }
    }

    override suspend fun getSectionById(sectionId: Int): Section? {
        return withContext(dispatcher) {
            sectionDao
                .getSectionById(sectionId)
                ?.toDomainForm()
        }
    }

    override suspend fun getSectionsByClassRoomId(classRoomId: Int): List<Section> {
        return withContext(dispatcher) {
            sectionDao
                .getSectionsByClassRoomId(classRoomId)
                .map { it.toDomainForm() }
        }
    }

    override suspend fun getSelectedSectionOnAssessment(assessmentId: Int): List<Section> {
        return withContext(dispatcher) {
            sectionDao
                .getSelectedSectionOnAssessment(assessmentId)
                .map { it.toDomainForm() }
        }
    }
}