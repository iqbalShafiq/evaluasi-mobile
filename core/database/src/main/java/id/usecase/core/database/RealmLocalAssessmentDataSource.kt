package id.usecase.core.database

import id.usecase.core.database.entities.AssessmentCategoryEntity
import id.usecase.core.database.entities.AssessmentEntity
import id.usecase.core.database.entities.AssessmentEventEntity
import id.usecase.core.database.entities.ClassRoomEntity
import id.usecase.core.database.entities.StudentEntity
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.models.Assessment
import id.usecase.core.domain.assessment.models.AssessmentCategory
import id.usecase.core.domain.assessment.models.AssessmentEvent
import id.usecase.core.domain.assessment.models.ClassRoom
import id.usecase.core.domain.assessment.models.Student
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId

class RealmLocalAssessmentDataSource(
    private val realm: Realm
) : LocalAssessmentDataSource {

    override suspend fun insertClassRoom(classRooms: ClassRoom) {
        withContext(Dispatchers.IO) {
            realm.write {
                val classRoomEntity = ClassRoomEntity().apply {
                    id = BsonObjectId()
                    name = classRooms.name
                    startPeriod = classRooms.startPeriod.toRealmInstant()
                    endPeriod = classRooms.endPeriod?.toRealmInstant()
                    createdTime = classRooms.createdTime.toRealmInstant()
                    lastModifiedTime = classRooms.lastModifiedTime.toRealmInstant()
                }

                copyToRealm(classRoomEntity)
            }
        }
    }

    override suspend fun insertStudentsToClassRoom(classRoomId: String, students: List<Student>) {
        withContext(Dispatchers.IO) {
            realm.write {
                val classRoomEntity = realm
                    .query<ClassRoomEntity>("id == $0", BsonObjectId(classRoomId))
                    .first()
                    .find()

                students.forEach { student ->
                    val studentEntity = StudentEntity().apply {
                        id = BsonObjectId()
                        name = student.name
                        identifier = student.identifier
                    }

                    classRoomEntity?.studentEntities?.add(studentEntity)
                }
            }
        }
    }

    override suspend fun deleteClassRoom(classRoomId: String) {
        withContext(Dispatchers.IO) {
            realm.write {
                val classRoomEntity = realm
                    .query<ClassRoomEntity>("id == $0", BsonObjectId(classRoomId))
                    .first()
                    .find() ?: throw Exception("Class room not found")

                delete(classRoomEntity)
            }
        }
    }

    override suspend fun deleteStudentFromClassRoom(studentId: String) {
        withContext(Dispatchers.IO) {
            realm.write {
                val studentEntity = realm
                    .query<StudentEntity>("id == $0", BsonObjectId(studentId))
                    .first()
                    .find() ?: throw Exception("Student not found")

                delete(studentEntity)
            }
        }
    }

    override suspend fun updateClassRoom(classRoom: ClassRoom) {
        withContext(Dispatchers.IO) {
            realm.write {
                val classRoomEntity = realm
                    .query<ClassRoomEntity>("id == $0", BsonObjectId(classRoom.id))
                    .first()
                    .find() ?: throw Exception("Class room not found")

                classRoomEntity.apply {
                    name = classRoom.name
                    startPeriod = classRoom.startPeriod.toRealmInstant()
                    endPeriod = classRoom.endPeriod?.toRealmInstant()
                    lastModifiedTime = classRoom.lastModifiedTime.toRealmInstant()
                }
            }
        }
    }

    override fun getClassRooms(): Flow<List<ClassRoom>> {
        return realm.query<ClassRoomEntity>()
            .asFlow()
            .map {
                it.list.map { classRoom ->
                    classRoom.toDomainForm()
                }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getClassRoomById(classRoomId: String): Flow<ClassRoom?> {
        return realm.query<ClassRoomEntity>("id == $0", classRoomId)
            .asFlow()
            .map {
                it.list.firstOrNull()?.toDomainForm()
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun insertAssessmentCategory(
        category: AssessmentCategory,
        classRoom: ClassRoom
    ) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentCategoryEntity = AssessmentCategoryEntity().apply {
                    id = BsonObjectId()
                    name = category.name
                    percentage = category.percentage
                    classRoomEntity = classRoom.toEntity()
                    createdTime = category.createdTime.toRealmInstant()
                    lastModifiedTime = category.lastModifiedTime.toRealmInstant()
                }

                copyFromRealm(assessmentCategoryEntity)
            }
        }
    }

    override suspend fun updateAssessmentCategory(category: AssessmentCategory) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentCategoryEntity =
                    realm.query<AssessmentCategoryEntity>("id == $0", category.id.toObjectId())
                        .first()
                        .find() ?: throw Exception("Assessment category not found")

                assessmentCategoryEntity.apply {
                    name = category.name
                    percentage = category.percentage
                    lastModifiedTime = category.lastModifiedTime.toRealmInstant()
                }

                copyFromRealm(assessmentCategoryEntity)
            }
        }
    }

    override suspend fun deleteAssessmentCategory(categoryId: String) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentCategoryEntity =
                    realm.query<AssessmentCategoryEntity>("id == $0", categoryId.toObjectId())
                        .first()
                        .find() ?: throw Exception("Assessment category not found")

                delete(assessmentCategoryEntity)
            }
        }
    }

    override fun getAssessmentCategories(): Flow<List<AssessmentCategory>> {
        return realm.query<AssessmentCategoryEntity>()
            .asFlow()
            .map { it.list.map { result -> result.toDomainForm() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getAssessmentCategoryById(categoryId: String): Flow<AssessmentCategory?> {
        return realm
            .query<AssessmentCategoryEntity>("id == $0", BsonObjectId(categoryId))
            .asFlow()
            .map { it.list.firstOrNull()?.toDomainForm() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun insertAssessmentEvent(
        event: AssessmentEvent,
        category: AssessmentCategory
    ) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentEventEntity = AssessmentEventEntity().apply {
                    id = BsonObjectId()
                    name = event.name
                    categoryEntity =
                        realm.query<AssessmentCategoryEntity>("id == $0", category.id.toObjectId())
                            .first()
                            .find() ?: throw Exception("Assessment category not found")
                    eventDate = event.eventDate.toRealmInstant()
                    createdTime = event.createdTime.toRealmInstant()
                    lastModifiedTime = event.lastModifiedTime.toRealmInstant()
                }

                copyFromRealm(assessmentEventEntity)
            }
        }
    }

    override suspend fun updateAssessmentEvent(event: AssessmentEvent) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentEventEntity = realm
                    .query<AssessmentEventEntity>("id == $0", event.id.toObjectId())
                    .first()
                    .find() ?: throw Exception("Assessment event not found")

                assessmentEventEntity.apply {
                    name = event.name
                    eventDate = event.eventDate.toRealmInstant()
                    lastModifiedTime = event.lastModifiedTime.toRealmInstant()
                }

                copyFromRealm(assessmentEventEntity)
            }
        }
    }

    override suspend fun deleteAssessmentEvent(eventId: String) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentEventEntity =
                    realm.query<AssessmentEventEntity>("id == $0", eventId.toObjectId())
                        .first()
                        .find() ?: throw Exception("Assessment event not found")

                delete(assessmentEventEntity)
            }
        }
    }

    override fun getAssessmentEvents(): Flow<List<AssessmentEvent>> {
        return realm.query<AssessmentEventEntity>()
            .asFlow()
            .map { it.list.map { result -> result.toDomainForm() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getAssessmentEventById(eventId: String): Flow<AssessmentEvent?> {
        return realm
            .query<AssessmentEventEntity>("id == $0", BsonObjectId(eventId))
            .asFlow()
            .map { it.list.firstOrNull()?.toDomainForm() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun insertAssessment(
        assessment: Assessment,
        event: AssessmentEvent,
        student: Student
    ) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentEntity = AssessmentEntity().apply {
                    id = BsonObjectId()
                    score = assessment.score
                    eventEntity = event.toEntity()
                    studentEntity = student.toEntity()
                    createdTime = assessment.createdTime.toRealmInstant()
                    lastModifiedTime = assessment.lastModifiedTime.toRealmInstant()
                }

                copyFromRealm(assessmentEntity)
            }
        }
    }

    override suspend fun updateAssessment(assessment: Assessment) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentEntity = realm
                    .query<AssessmentEntity>("id == $0", assessment.id.toObjectId())
                    .first()
                    .find() ?: throw Exception("Assessment not found")

                assessmentEntity.apply {
                    score = assessment.score
                    lastModifiedTime = assessment.lastModifiedTime.toRealmInstant()
                }

                copyFromRealm(assessmentEntity)
            }
        }
    }

    override suspend fun deleteAssessment(assessmentId: String) {
        withContext(Dispatchers.IO) {
            realm.write {
                val assessmentEntity = realm
                    .query<AssessmentEntity>("id == $0", assessmentId.toObjectId())
                    .first()
                    .find() ?: throw Exception("Assessment not found")

                delete(assessmentEntity)
            }
        }
    }

    override fun getAssessments(): Flow<List<Assessment>> {
        return realm.query<AssessmentEntity>()
            .asFlow()
            .map { it.list.map { result -> result.toDomainForm() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getAssessmentById(assessmentId: String): Flow<Assessment?> {
        return realm
            .query<AssessmentEntity>("id == $0", BsonObjectId(assessmentId))
            .asFlow()
            .map { it.list.firstOrNull()?.toDomainForm() }
            .flowOn(Dispatchers.IO)
    }
}