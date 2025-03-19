package id.usecase.core.data.sync

import id.usecase.core.data.sync.model.AssessmentNetworkModel
import id.usecase.core.data.sync.model.CategoryNetworkModel
import id.usecase.core.data.sync.model.ClassRoomNetworkModel
import id.usecase.core.data.sync.model.EventNetworkModel
import id.usecase.core.data.sync.model.SectionNetworkModel
import id.usecase.core.data.sync.model.StudentNetworkModel
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.section.Section
import id.usecase.core.domain.assessment.model.student.Student
import id.usecase.core.domain.sync.EntityType
import id.usecase.core.domain.sync.SyncableEntity
import java.text.SimpleDateFormat
import java.util.Locale

class EntitySyncFactory {
    fun createSyncableEntity(entityType: EntityType, entity: Any): SyncableEntity {
        return when (entityType) {
            EntityType.CLASS_ROOM -> ClassRoomSyncable(entity as ClassRoom)
            EntityType.STUDENT -> StudentSyncable(entity as Student)
            EntityType.CATEGORY -> CategorySyncable(entity as Category)
            EntityType.SECTION -> SectionSyncable(entity as Section)
            EntityType.EVENT -> EventSyncable(entity as Event)
            EntityType.ASSESSMENT -> AssessmentSyncable(entity as Assessment)
            EntityType.EVENT_SECTION -> TODO()
        }
    }

    inner class ClassRoomSyncable(private val entity: ClassRoom) : SyncableEntity {
        override val id: String get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return ClassRoomNetworkModel(
                id = entity.id,
                teacherId = entity.teacherId,
                name = entity.name,
                subject = entity.subject,
                description = entity.description,
                startPeriod = entity.startPeriod,
                longPeriod = entity.longPeriod,
                schedule = entity.schedule,
                lastModifiedStatus = 'I',
                createdTime = SimpleDateFormat(
                    "yyyy-MM-DD'T'HH:mm:ss",
                    Locale.getDefault()
                ).format(entity.createdTime),
                updatedTime = SimpleDateFormat(
                    "yyyy-MM-DD'T'HH:mm:ss",
                    Locale.getDefault()
                ).format(entity.lastModifiedTime),
            )
        }
    }

    inner class StudentSyncable(private val entity: Student) : SyncableEntity {
        override val id: String get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return StudentNetworkModel(
                id = entity.id,
                name = entity.name,
                identifier = entity.identifier,
                classRoomId = entity.classRoomId,
                createdTime = entity.createdTime,
                updatedTime = entity.lastModifiedTime
            )
        }
    }

    inner class CategorySyncable(private val entity: Category) : SyncableEntity {
        override val id: String get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return CategoryNetworkModel(
                id = entity.id,
                name = entity.name,
                createdTime = entity.createdTime,
                updatedTime = entity.lastModifiedTime,
                percentage = entity.percentage,
                classRoomId = entity.classRoomId
            )
        }
    }

    inner class SectionSyncable(private val entity: Section) : SyncableEntity {
        override val id: String get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return SectionNetworkModel(
                id = entity.id,
                name = entity.name,
                createdTime = entity.createdTime,
                updatedTime = entity.lastModifiedTime,
                classRoomId = entity.classRoomId,
                topics = entity.topics
            )
        }
    }

    inner class EventSyncable(private val entity: Event) : SyncableEntity {
        override val id: String get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return EventNetworkModel(
                id = entity.id,
                name = entity.name,
                createdTime = entity.createdTime,
                updatedTime = entity.lastModifiedTime,
                eventDate = entity.eventDate,
                categoryId = entity.categoryId,
                purpose = entity.purpose
            )
        }
    }

    inner class AssessmentSyncable(private val entity: Assessment) : SyncableEntity {
        override val id: String get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return AssessmentNetworkModel(
                id = entity.id,
                createdTime = entity.createdTime,
                updatedTime = entity.lastModifiedTime,
                studentId = entity.studentId,
                eventId = entity.eventId,
                score = entity.score
            )
        }
    }
}