package id.usecase.core.domain.sync

import kotlinx.coroutines.flow.Flow

interface SyncDataSource {
    suspend fun insertSyncItem(syncEntity: SyncModel): Long

    suspend fun insertSyncItems(syncEntities: List<SyncModel>): List<Long>

    suspend fun updateSyncItem(syncEntity: SyncModel)

    suspend fun getSyncItemsByStatus(status: SyncStatus): List<SyncModel>

    suspend fun getSyncItemForEntity(entityId: String, entityType: EntityType): SyncModel?

    suspend fun getPendingSyncItems(
        statuses: List<SyncStatus> = listOf(SyncStatus.PENDING, SyncStatus.FAILED),
        limit: Int = 50
    ): List<SyncModel>

    suspend fun updateSyncStatus(id: Int, newStatus: SyncStatus)

    suspend fun updateSyncResult(
        id: Int,
        newStatus: SyncStatus,
        timestamp: Long,
        reason: String? = null
    )

    fun observePendingSyncCount(
        statuses: List<SyncStatus> = listOf(
            SyncStatus.PENDING,
            SyncStatus.FAILED,
            SyncStatus.SYNCING
        )
    ): Flow<Int>

    suspend fun cleanupOldSyncItems(status: SyncStatus = SyncStatus.SYNCED, cutoffTime: Long): Int
}