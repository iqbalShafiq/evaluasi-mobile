package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import id.usecase.core.database.entities.ClassRoomEntity

@Dao
interface ClassRoomDao {
    @Upsert
    suspend fun upsert(classRoom: ClassRoomEntity): Long

    @Query("SELECT * FROM class_rooms ORDER BY last_modified_time DESC")
    suspend fun getAllClassRooms(): List<ClassRoomEntity>

    @Query("SELECT * FROM class_rooms WHERE id = :id")
    suspend fun getClassRoomById(id: Int): ClassRoomEntity?

    @Delete
    suspend fun delete(classRoom: ClassRoomEntity)
}