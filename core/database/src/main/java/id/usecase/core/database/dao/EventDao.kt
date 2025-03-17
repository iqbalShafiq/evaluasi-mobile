package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import id.usecase.core.database.entities.EventEntity
import id.usecase.core.database.entities.EventSectionCrossRef

@Dao
interface EventDao {
    @Upsert
    suspend fun upsert(event: EventEntity): Long

    @Upsert
    suspend fun upsertEventSection(crossRef: List<EventSectionCrossRef>): List<Long>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Int): EventEntity?

    @Query(
        """
        SELECT events.* FROM events
        INNER JOIN categories ON events.category_id = categories.id
        WHERE categories.class_room_id = :classRoomId
        ORDER BY events.last_modified_time DESC
        """
    )
    suspend fun getEventsByClassRoomId(classRoomId: Int): List<EventEntity>

    @Query(
        """
        SELECT events.* FROM events
        INNER JOIN event_section_cross_ref ON events.id = event_section_cross_ref.eventId
        WHERE event_section_cross_ref.sectionId = :sectionId
        ORDER BY events.last_modified_time DESC
        """
    )
    suspend fun getEventSectionCrossRef(eventId: Int, sectionId: Int): EventSectionCrossRef?

    @Query("SELECT * FROM events WHERE category_id = :categoryId")
    suspend fun getEventsByCategoryId(categoryId: Int): List<EventEntity>

    @Delete
    suspend fun delete(event: EventEntity)

    @Delete
    suspend fun deleteEventSection(crossRef: List<EventSectionCrossRef>)
}