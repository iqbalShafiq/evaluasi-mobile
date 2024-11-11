package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import id.usecase.core.database.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsert(categories: List<CategoryEntity>): List<Long>

    @Upsert
    suspend fun upsert(category: CategoryEntity): Long

    @Query("SELECT * FROM categories WHERE class_room_id = :classRoomId")
    suspend fun getCategoriesByClassRoomId(classRoomId: Int): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Query("SELECT * FROM categories WHERE id IN (:categoryIds)")
    suspend fun getCategoriesByIds(categoryIds: List<Int>): List<CategoryEntity>

    @Delete
    suspend fun delete(category: CategoryEntity)
}