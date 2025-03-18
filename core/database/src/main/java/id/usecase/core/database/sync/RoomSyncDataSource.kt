package id.usecase.core.database.sync

import id.usecase.core.database.dao.SyncDao
import id.usecase.core.domain.sync.EntityType
import id.usecase.core.domain.sync.SyncDataSource
import id.usecase.core.domain.sync.SyncModel
import id.usecase.core.domain.sync.SyncStatus
import kotlinx.coroutines.flow.Flow

class RoomSyncDataSource(private val syncDao: SyncDao) : SyncDataSource {
    override suspend fun insertSyncItem(syncEntity: SyncModel): Long {
        return syncDao.insertSyncItem(syncEntity.toEntity())
    }

    override suspend fun insertSyncItems(syncEntities: List<SyncModel>): List<Long> {
        return syncDao.insertSyncItems(syncEntities.map { it.toEntity() })
    }

    override suspend fun updateSyncItem(syncEntity: SyncModel) {
        syncDao.updateSyncItem(syncEntity.toEntity())
    }

    override suspend fun getSyncItemsByStatus(status: SyncStatus): List<SyncModel> {
        return syncDao.getSyncItemsByStatus(status).map { it.toModel() }
    }

    override suspend fun getSyncItemForEntity(
        entityId: String,
        entityType: EntityType
    ): SyncModel? {
        return syncDao.getSyncItemForEntity(entityId, entityType)?.toModel()
    }

    override suspend fun getPendingSyncItems(
        statuses: List<SyncStatus>,
        limit: Int
    ): List<SyncModel> {
        return syncDao.getPendingSyncItems(statuses, limit).map { it.toModel() }
    }

    override suspend fun updateSyncStatus(
        id: Int,
        newStatus: SyncStatus
    ) {
        syncDao.updateSyncStatus(id, newStatus)
    }

    override suspend fun updateSyncResult(
        id: Int,
        newStatus: SyncStatus,
        timestamp: Long,
        reason: String?
    ) {
        syncDao.updateSyncResult(id, newStatus, timestamp, reason)
    }

    override fun observePendingSyncCount(statuses: List<SyncStatus>): Flow<Int> {
        return syncDao.observePendingSyncCount(statuses)
    }

    override suspend fun cleanupOldSyncItems(
        status: SyncStatus,
        cutoffTime: Long
    ): Int {
        return syncDao.cleanupOldSyncItems(status, cutoffTime)
    }
}