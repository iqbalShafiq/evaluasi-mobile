package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import id.usecase.core.database.entities.ClassRoomEntity

@Dao
interface ClassRoomDao {

    @Transaction
    suspend fun upsertAndGetId(classRoom: ClassRoomEntity): String {
        upsert(classRoom)
        return classRoom.id
    }

    @Upsert
    suspend fun upsert(classRoom: ClassRoomEntity)

    @Query("SELECT * FROM class_rooms ORDER BY last_modified_time DESC")
    suspend fun getAllClassRooms(): List<ClassRoomEntity>

    @Query("SELECT * FROM class_rooms WHERE name LIKE '%' || :query || '%' ORDER BY last_modified_time DESC")
    suspend fun searchClassRooms(query: String): List<ClassRoomEntity>

    @Query("SELECT * FROM class_rooms WHERE id = :id")
    suspend fun getClassRoomById(id: String): ClassRoomEntity?

    @Delete
    suspend fun delete(classRoom: ClassRoomEntity)
}