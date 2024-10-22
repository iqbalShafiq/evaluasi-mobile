package id.usecase.core.database

import id.usecase.core.database.entities.AssessmentEntity
import id.usecase.core.database.entities.CategoryEntity
import id.usecase.core.database.entities.ClassRoomEntity
import id.usecase.core.database.entities.EventEntity
import id.usecase.core.database.entities.StudentEntity
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.student.Student

fun StudentEntity.toDomainForm() = Student(
    id = id,
    classRoomId = classRoomId,
    name = name,
    identifier = identifier,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun Student.toEntity() = StudentEntity(
    id = id,
    classRoomId = classRoomId,
    name = name,
    identifier = identifier,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun ClassRoomEntity.toDomainForm() = ClassRoom(
    id = id,
    name = name,
    subject = subject,
    startPeriod = startPeriod,
    endPeriod = endPeriod,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime,
)

fun ClassRoom.toEntity() = ClassRoomEntity(
    id = id,
    name = name,
    subject = subject,
    startPeriod = startPeriod,
    endPeriod = endPeriod,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime,
)

fun CategoryEntity.toDomainForm() = Category(
    id = id,
    name = name,
    percentage = percentage,
    classRoomId = classRoomId,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    percentage = percentage,
    classRoomId = classRoomId,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun EventEntity.toDomainForm() = Event(
    id = id,
    name = name,
    categoryId = categoryId,
    eventDate = eventDate,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun Event.toEntity() = EventEntity(
    id = id,
    name = name,
    categoryId = categoryId,
    eventDate = eventDate,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun AssessmentEntity.toDomainForm() = Assessment(
    id = id,
    score = score,
    studentId = studentId,
    eventId = eventId,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun Assessment.toEntity() = AssessmentEntity(
    id = id,
    score = score,
    studentId = studentId,
    eventId = eventId,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)