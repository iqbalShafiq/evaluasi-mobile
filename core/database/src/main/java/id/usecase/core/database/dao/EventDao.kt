package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import id.usecase.core.database.entities.EventEntity
import id.usecase.core.database.entities.EventSectionCrossRef

@Dao
interface EventDao {
    @Transaction
    suspend fun upsertAndGetId(event: EventEntity): String {
        upsert(event)
        return event.id
    }

    @Transaction
    suspend fun upsertEventSectionsAndGetIds(
        crossRef: List<EventSectionCrossRef>
    ): List<String> {
        upsertEventSection(crossRef)
        return crossRef.map { it.eventSectionId }
    }

    @Upsert
    suspend fun upsert(event: EventEntity)

    @Upsert
    suspend fun upsertEventSection(crossRef: List<EventSectionCrossRef>)

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    @Query(
        """
        SELECT events.* FROM events
        INNER JOIN categories ON events.category_id = categories.id
        WHERE categories.class_room_id = :classRoomId
        ORDER BY events.last_modified_time DESC
        """
    )
    suspend fun getEventsByClassRoomId(classRoomId: String): List<EventEntity>

    @Query(
        """
        SELECT * FROM event_section_cross_ref
        WHERE event_section_cross_ref.eventSectionId = :eventSectionId
        """
    )
    suspend fun getEventSectionCrossRef(eventSectionId: String): EventSectionCrossRef?

    @Query(
        """
        SELECT * FROM event_section_cross_ref
        WHERE event_section_cross_ref.eventSectionId IN (:eventSectionIdList)
        """
    )
    suspend fun getEventSectionCrossRef(eventSectionIdList: List<String>): List<EventSectionCrossRef>

    @Query("SELECT * FROM events WHERE category_id = :categoryId")
    suspend fun getEventsByCategoryId(categoryId: String): List<EventEntity>

    @Delete
    suspend fun delete(event: EventEntity)

    @Delete
    suspend fun deleteEventSection(crossRef: List<EventSectionCrossRef>)
}