package id.usecase.core.database

import id.usecase.core.database.entities.AssessmentEntity
import id.usecase.core.database.entities.CategoryEntity
import id.usecase.core.database.entities.ClassRoomEntity
import id.usecase.core.database.entities.EventEntity
import id.usecase.core.database.entities.EventSectionCrossRef
import id.usecase.core.database.entities.SectionEntity
import id.usecase.core.database.entities.StudentEntity
import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import id.usecase.core.domain.assessment.model.analytics.CategoryScore
import id.usecase.core.domain.assessment.model.analytics.MonthlyScore
import id.usecase.core.domain.assessment.model.analytics.PerformanceScore
import id.usecase.core.domain.assessment.model.analytics.StudentProgress
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.section.EventSection
import id.usecase.core.domain.assessment.model.section.Section
import id.usecase.core.domain.assessment.model.student.Student
import id.usecase.core.database.model.analytics.CategoryAnalysis as CategoryAnalysisDb
import id.usecase.core.database.model.analytics.CategoryScore as CategoryScoreDb
import id.usecase.core.database.model.analytics.MonthlyScore as MonthlyScoreDb
import id.usecase.core.database.model.analytics.PerformanceScore as PerformanceScoreDb
import id.usecase.core.database.model.analytics.StudentProgress as StudentProgressDb

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
    longPeriod = longPeriod,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime,
    schedule = schedule,
    description = description
)

fun ClassRoom.toEntity() = ClassRoomEntity(
    id = id,
    name = name,
    subject = subject,
    startPeriod = startPeriod,
    longPeriod = longPeriod,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime,
    schedule = schedule,
    description = description
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
    purpose = purpose,
    categoryId = categoryId,
    eventDate = eventDate,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun Event.toEntity() = EventEntity(
    id = id,
    name = name,
    purpose = purpose,
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

fun MonthlyScoreDb.toDomainForm() = MonthlyScore(
    month = month,
    averageScore = averageScore
)

fun CategoryScoreDb.toDomainForm() = CategoryScore(
    categoryName = categoryName,
    totalAssessments = totalAssessments.toFloat()
)

fun PerformanceScoreDb.toDomainForm() = PerformanceScore(
    performanceLevel = performanceLevel,
    averageScore = averageScore
)

fun StudentProgressDb.toDomainForm() = StudentProgress(
    studentName = studentName,
    progressPercentage = progressPercentage,
    lastUpdated = lastUpdated
)

fun CategoryAnalysisDb.toDomainForm() = CategoryAnalysis(
    categoryName = categoryName,
    averageScore = averageScore
)

fun Section.toEntity() = SectionEntity(
    id = id,
    name = name,
    topics = topics,
    classRoomId = classRoomId,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun SectionEntity.toDomainForm() = Section(
    id = id,
    name = name,
    topics = topics,
    classRoomId = classRoomId,
    createdTime = createdTime,
    lastModifiedTime = lastModifiedTime
)

fun EventSection.toEntity() = EventSectionCrossRef(
    eventId = eventId,
    sectionId = sectionId
)

