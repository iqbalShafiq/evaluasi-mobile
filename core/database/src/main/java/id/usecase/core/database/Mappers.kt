package id.usecase.core.database

import id.usecase.core.database.entities.AssessmentCategoryEntity
import id.usecase.core.database.entities.AssessmentEntity
import id.usecase.core.database.entities.AssessmentEventEntity
import id.usecase.core.database.entities.ClassRoomEntity
import id.usecase.core.database.entities.StudentEntity
import id.usecase.core.domain.assessment.models.Assessment
import id.usecase.core.domain.assessment.models.AssessmentCategory
import id.usecase.core.domain.assessment.models.AssessmentEvent
import id.usecase.core.domain.assessment.models.ClassRoom
import id.usecase.core.domain.assessment.models.Student
import org.mongodb.kbson.ObjectId

fun StudentEntity.toDomainForm() = Student(
    id = this.id.toString(),
    name = this.name,
    identifier = this.identifier,
    createdTime = this.createdTime.toJavaInstant(),
    lastModifiedTime = this.lastModifiedTime.toJavaInstant()
)

fun Student.toEntity() = StudentEntity().apply {
    id = ObjectId(this@toEntity.id)
    name = this@toEntity.name
    identifier = this@toEntity.identifier
    createdTime = this@toEntity.createdTime.toRealmInstant()
    lastModifiedTime = this@toEntity.lastModifiedTime.toRealmInstant()
}

fun ClassRoomEntity.toDomainForm() = ClassRoom(
    id = this.id.toString(),
    name = this.name,
    subject = this.subject,
    startPeriod = this.startPeriod.toJavaInstant(),
    endPeriod = this.endPeriod?.toJavaInstant(),
    students = this.studentEntities.map { it.toDomainForm() },
    createdTime = this.createdTime.toJavaInstant(),
    lastModifiedTime = this.lastModifiedTime.toJavaInstant()
)

fun ClassRoom.toEntity() = ClassRoomEntity().apply {
    id = ObjectId(this@toEntity.id)
    name = this@toEntity.name
    startPeriod = this@toEntity.startPeriod.toRealmInstant()
    endPeriod = this@toEntity.endPeriod?.toRealmInstant()
    createdTime = this@toEntity.createdTime.toRealmInstant()
    lastModifiedTime = this@toEntity.lastModifiedTime.toRealmInstant()
    studentEntities.addAll(this@toEntity.students.map { it.toEntity() })
}

fun AssessmentCategoryEntity.toDomainForm() = AssessmentCategory(
    id = this.id.toString(),
    name = this.name,
    percentage = this.percentage,
    classRoom = this.classRoomEntity?.toDomainForm(),
    createdTime = this.createdTime.toJavaInstant(),
    lastModifiedTime = this.lastModifiedTime.toJavaInstant()
)

fun AssessmentCategory.toEntity() = AssessmentCategoryEntity().apply {
    id = ObjectId(this@toEntity.id)
    name = this@toEntity.name
    percentage = this@toEntity.percentage
    classRoomEntity = this@toEntity.classRoom?.toEntity()
    createdTime = this@toEntity.createdTime.toRealmInstant()
    lastModifiedTime = this@toEntity.lastModifiedTime.toRealmInstant()
}

fun AssessmentEventEntity.toDomainForm() = AssessmentEvent(
    id = this.id.toString(),
    name = this.name,
    eventDate = this.eventDate.toJavaInstant(),
    category = this.categoryEntity?.toDomainForm(),
    createdTime = this.createdTime.toJavaInstant(),
    lastModifiedTime = this.lastModifiedTime.toJavaInstant()
)

fun AssessmentEvent.toEntity() = AssessmentEventEntity().apply {
    id = ObjectId(this@toEntity.id)
    name = this@toEntity.name
    categoryEntity = this@toEntity.category?.toEntity()
    eventDate = this@toEntity.eventDate.toRealmInstant()
    createdTime = this@toEntity.createdTime.toRealmInstant()
    lastModifiedTime = this@toEntity.lastModifiedTime.toRealmInstant()
}

fun AssessmentEntity.toDomainForm() = Assessment(
    id = this.id.toString(),
    score = this.score,
    student = this.studentEntity?.toDomainForm(),
    event = this.eventEntity?.toDomainForm(),
    createdTime = this.createdTime.toJavaInstant(),
    lastModifiedTime = this.lastModifiedTime.toJavaInstant()
)