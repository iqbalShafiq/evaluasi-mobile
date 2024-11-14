package id.usecase.assessment.data

import id.usecase.assessment.domain.CategoryRepository
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.category.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : CategoryRepository {
    override suspend fun upsertCategories(categories: List<Category>): DataResult<List<Category>> {
        return withContext(dispatcher) {
            try {
                val insertedIds = dataSource
                    .upsertCategories(categories)
                    .map { it.toInt() }
                val insertedCategories = dataSource.getCategoriesByIds(insertedIds)
                return@withContext DataResult.Success(insertedCategories)
            } catch (e: Exception) {
                throw e
                return@withContext DataResult.Error(e)
            }
        }
    }

    override suspend fun upsertCategory(category: Category): DataResult<Category?> {
        return withContext(dispatcher) {
            try {
                val id = dataSource
                    .upsertCategory(category)
                    .toInt()
                val category = dataSource.getCategoryById(id)

                return@withContext DataResult.Success(category)
            } catch (e: Exception) {
                throw e
                return@withContext DataResult.Error(e)
            }
        }
    }

    override fun getCategoriesByClassRoomId(classRoomId: Int): Flow<DataResult<List<Category>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val categories: List<Category> = dataSource.getCategoriesByClassRoomId(classRoomId)
                emit(DataResult.Success(categories))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getCategoryById(categoryId: Int): Flow<DataResult<Category?>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val category: Category? = dataSource.getCategoryById(categoryId)
                if (category == null) {
                    emit(
                        DataResult.Error(
                            Exception("Category not found")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(category))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteCategory(category: Category) {
        withContext(dispatcher) {
            try {
                dataSource.deleteCategory(category)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}