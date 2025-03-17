package id.usecase.core.domain.sync

import kotlinx.coroutines.flow.Flow

interface ISyncService {
    suspend fun <T : Any> markForSync(entity: T, entityType: EntityType)
    suspend fun <T : Any> markMultipleForSync(entities: List<T>, entityType: EntityType)
    fun scheduleImmediateSync()
    suspend fun getPendingSyncItems(limit: Int = 50): List<SyncEntity>
    suspend fun updateSyncResult(id: Int, isSuccess: Boolean, errorMessage: String? = null)
    fun observePendingSyncCount(): Flow<Int>
    suspend fun cleanupOldSyncRecords(olderThan: Long = 7 * 24 * 60 * 60 * 1000L)
}