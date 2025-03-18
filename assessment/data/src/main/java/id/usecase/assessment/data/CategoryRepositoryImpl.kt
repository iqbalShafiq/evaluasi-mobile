@file:Suppress("FoldInitializerAndIfToElvis")

package id.usecase.assessment.data

import id.usecase.assessment.domain.CategoryRepository
import id.usecase.core.data.sync.SyncService
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.sync.EntityType
import id.usecase.core.domain.utils.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val syncService: SyncService,
    private val dispatcher: CoroutineDispatcher
) : CategoryRepository {
    override suspend fun upsertCategories(categories: List<Category>): DataResult<List<Category>> {
        return withContext(dispatcher) {
            val insertedIds = dataSource.upsertCategories(categories)
            val insertedCategories = dataSource.getCategoriesByIds(insertedIds)
            if (insertedCategories.isNotEmpty()) {
                syncService.markMultipleForSync(
                    entities = insertedCategories,
                    entityType = EntityType.CATEGORY
                )
            }

            return@withContext DataResult.Success(insertedCategories)
        }
    }

    override suspend fun upsertCategory(category: Category): DataResult<Category?> {
        return withContext(dispatcher) {
            val id = dataSource.upsertCategory(category)
            val result = dataSource.getCategoryById(id)
            if (result != null) {
                syncService.markForSync(
                    entity = result,
                    entityType = EntityType.CATEGORY
                )
            }
            return@withContext DataResult.Success(result)
        }
    }

    override fun getCategoriesByClassRoomId(classRoomId: String): Flow<DataResult<List<Category>>> {
        return flow {
            emit(DataResult.Loading)
            val categories: List<Category> = dataSource.getCategoriesByClassRoomId(classRoomId)
            emit(DataResult.Success(categories))
        }.flowOn(dispatcher)
    }

    override fun getCategoryById(categoryId: String): Flow<DataResult<Category?>> {
        return flow {
            emit(DataResult.Loading)
            val category: Category? = dataSource.getCategoryById(categoryId)
            if (category == null) throw Exception("Category not found")

            emit(DataResult.Success(category))
        }.flowOn(dispatcher)
    }

    override suspend fun deleteCategory(category: Category) {
        withContext(dispatcher) {
            dataSource.deleteCategory(category)
        }
    }
}