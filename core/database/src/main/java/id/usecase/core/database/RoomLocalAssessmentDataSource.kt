package id.usecase.core.database

import id.usecase.core.database.dao.AssessmentDao
import id.usecase.core.database.dao.CategoryDao
import id.usecase.core.database.dao.ClassRoomDao
import id.usecase.core.database.dao.EventDao
import id.usecase.core.database.dao.StudentDao
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.student.Student
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RoomLocalAssessmentDataSource(
    private val classRoomDao: ClassRoomDao,
    private val categoryDao: CategoryDao,
    private val eventDao: EventDao,
    private val assessmentDao: AssessmentDao,
    private val studentDao: StudentDao,
    private val dispatcher: CoroutineDispatcher
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

    override suspend fun insertStudent(students: Student) {
        withContext(dispatcher) {
            studentDao.upsert(students.toEntity())
        }
    }

    override suspend fun insertStudents(students: List<Student>) {
        withContext(dispatcher) {
            studentDao.upsert(
                students.map {
                    it.toEntity()
                }
            )
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

    override suspend fun upsertEvent(event: Event) {
        withContext(dispatcher) {
            eventDao.upsert(event.toEntity())
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

    override suspend fun upsertAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            assessmentDao.upsert(assessment.toEntity())
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

    override suspend fun deleteAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            assessmentDao.delete(assessment.toEntity())
        }
    }
}