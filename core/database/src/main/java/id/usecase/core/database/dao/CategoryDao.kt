package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import id.usecase.core.database.entities.CategoryEntity

@Dao
interface CategoryDao {

    @Transaction
    suspend fun upsertAndGetId(category: CategoryEntity): String {
        upsert(category)
        return category.id
    }

    @Transaction
    suspend fun upsertAndGetId(categories: List<CategoryEntity>): List<String> {
        upsert(categories)
        return categories.map { it.id }
    }

    @Upsert
    suspend fun upsert(categories: List<CategoryEntity>)

    @Upsert
    suspend fun upsert(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE class_room_id = :classRoomId")
    suspend fun getCategoriesByClassRoomId(classRoomId: String): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Query("SELECT * FROM categories WHERE id IN (:categoryIds)")
    suspend fun getCategoriesByIds(categoryIds: List<String>): List<CategoryEntity>

    @Delete
    suspend fun delete(category: CategoryEntity)
}