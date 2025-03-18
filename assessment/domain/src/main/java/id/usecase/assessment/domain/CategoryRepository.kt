package id.usecase.assessment.domain

import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.model.assessment.category.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun upsertCategories(categories: List<Category>): DataResult<List<Category>>
    suspend fun upsertCategory(category: Category): DataResult<Category?>
    fun getCategoriesByClassRoomId(classRoomId: String): Flow<DataResult<List<Category>>>
    fun getCategoryById(categoryId: String): Flow<DataResult<Category?>>
    suspend fun deleteCategory(category: Category)
}