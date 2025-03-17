package id.usecase.core.database.sync

import id.usecase.core.data.sync.model.AssessmentNetworkModel
import id.usecase.core.data.sync.model.CategoryNetworkModel
import id.usecase.core.data.sync.model.ClassRoomNetworkModel
import id.usecase.core.data.sync.model.EventNetworkModel
import id.usecase.core.data.sync.model.SectionNetworkModel
import id.usecase.core.data.sync.model.StudentNetworkModel
import id.usecase.core.database.entities.AssessmentEntity
import id.usecase.core.database.entities.CategoryEntity
import id.usecase.core.database.entities.ClassRoomEntity
import id.usecase.core.database.entities.EventEntity
import id.usecase.core.database.entities.SectionEntity
import id.usecase.core.database.entities.StudentEntity
import id.usecase.core.domain.sync.EntityType
import id.usecase.core.domain.sync.SyncableEntity

class EntitySyncFactory {
    fun createSyncableEntity(entityType: EntityType, entity: Any): SyncableEntity {
        return when (entityType) {
            EntityType.CLASS_ROOM -> ClassRoomSyncable(entity as ClassRoomEntity)
            EntityType.STUDENT -> StudentSyncable(entity as StudentEntity)
            EntityType.CATEGORY -> CategorySyncable(entity as CategoryEntity)
            EntityType.SECTION -> SectionSyncable(entity as SectionEntity)
            EntityType.EVENT -> EventSyncable(entity as EventEntity)
            EntityType.ASSESSMENT -> AssessmentSyncable(entity as AssessmentEntity)
        }
    }

    inner class ClassRoomSyncable(private val entity: ClassRoomEntity) : SyncableEntity {
        override val id: Int get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return ClassRoomNetworkModel(
                id = entity.id,
                name = entity.name,
                subject = entity.subject,
                description = entity.description,
                startPeriod = entity.startPeriod,
                longPeriod = entity.longPeriod,
                schedule = entity.schedule,
                createdTime = entity.createdTime,
                lastModifiedTime = entity.lastModifiedTime
            )
        }
    }

    inner class StudentSyncable(private val entity: StudentEntity) : SyncableEntity {
        override val id: Int get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return StudentNetworkModel(
                id = entity.id,
                name = entity.name,
                identifier = entity.identifier,
                classRoomId = entity.classRoomId,
                createdTime = entity.createdTime,
                lastModifiedTime = entity.lastModifiedTime
            )
        }
    }

    inner class CategorySyncable(private val entity: CategoryEntity) : SyncableEntity {
        override val id: Int get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return CategoryNetworkModel(
                id = entity.id,
                name = entity.name,
                createdTime = entity.createdTime,
                lastModifiedTime = entity.lastModifiedTime,
                percentage = entity.percentage,
                classRoomId = entity.classRoomId
            )
        }
    }

    inner class SectionSyncable(private val entity: SectionEntity) : SyncableEntity {
        override val id: Int get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return SectionNetworkModel(
                id = entity.id,
                name = entity.name,
                createdTime = entity.createdTime,
                lastModifiedTime = entity.lastModifiedTime,
                classRoomId = entity.classRoomId,
                topics = entity.topics
            )
        }
    }

    inner class EventSyncable(private val entity: EventEntity) : SyncableEntity {
        override val id: Int get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return EventNetworkModel(
                id = entity.id,
                name = entity.name,
                createdTime = entity.createdTime,
                lastModifiedTime = entity.lastModifiedTime,
                eventDate = entity.eventDate,
                categoryId = entity.categoryId,
                purpose = entity.purpose
            )
        }
    }

    inner class AssessmentSyncable(private val entity: AssessmentEntity) : SyncableEntity {
        override val id: Int get() = entity.id
        override val lastModifiedTime: Long get() = entity.lastModifiedTime

        override fun toNetworkModel(): Any {
            return AssessmentNetworkModel(
                id = entity.id,
                createdTime = entity.createdTime,
                lastModifiedTime = entity.lastModifiedTime,
                studentId = entity.studentId,
                eventId = entity.eventId,
                score = entity.score
            )
        }
    }
}