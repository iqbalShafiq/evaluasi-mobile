package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import id.usecase.core.database.entities.SectionEntity

@Dao
interface SectionDao {
    @Upsert
    suspend fun upsert(section: List<SectionEntity>): List<Long>

    @Query("SELECT * FROM sections WHERE id = :id")
    suspend fun getSectionById(id: Int): SectionEntity?

    @Query("SELECT * FROM sections WHERE class_room_id = :classRoomId")
    suspend fun getSectionsByClassRoomId(classRoomId: Int): List<SectionEntity>

    @Delete
    suspend fun delete(section: SectionEntity)
}