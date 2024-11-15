package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import id.usecase.core.database.entities.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Upsert
    suspend fun upsert(event: EventEntity): Long

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Int): EventEntity?

    @Query(
        """
        SELECT events.* FROM events
        INNER JOIN categories ON events.category_id = categories.id
        WHERE categories.class_room_id = :classRoomId
        """
    )
    suspend fun getEventsByClassRoomId(classRoomId: Int): List<EventEntity>

    @Query("SELECT * FROM events WHERE category_id = :categoryId")
    suspend fun getEventsByCategoryId(categoryId: Int): List<EventEntity>

    @Delete
    suspend fun delete(event: EventEntity)
}