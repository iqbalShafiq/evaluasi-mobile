package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.usecase.core.database.entities.SyncEntity
import id.usecase.core.domain.sync.EntityType
import id.usecase.core.domain.sync.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncItem(syncEntity: SyncEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncItems(syncEntities: List<SyncEntity>): List<Long>

    @Update
    suspend fun updateSyncItem(syncEntity: SyncEntity)

    @Query("SELECT * FROM sync_items WHERE syncStatus = :status ORDER BY priority DESC, createdTime ASC")
    suspend fun getSyncItemsByStatus(status: SyncStatus): List<SyncEntity>

    @Query("SELECT * FROM sync_items WHERE entityId = :entityId AND entityType = :entityType")
    suspend fun getSyncItemForEntity(entityId: String, entityType: EntityType): SyncEntity?

    @Query("SELECT * FROM sync_items WHERE syncStatus IN (:statuses) ORDER BY priority DESC, attemptCount ASC, createdTime ASC LIMIT :limit")
    suspend fun getPendingSyncItems(
        statuses: List<SyncStatus> = listOf(SyncStatus.PENDING, SyncStatus.FAILED),
        limit: Int = 50
    ): List<SyncEntity>

    @Query("UPDATE sync_items SET syncStatus = :newStatus WHERE id = :id")
    suspend fun updateSyncStatus(id: Int, newStatus: SyncStatus)

    @Query("UPDATE sync_items SET syncStatus = :newStatus, attemptCount = attemptCount + 1, lastSyncAttempt = :timestamp, failureReason = :reason WHERE id = :id")
    suspend fun updateSyncResult(
        id: Int,
        newStatus: SyncStatus,
        timestamp: Long,
        reason: String? = null
    )

    @Query("SELECT COUNT(*) FROM sync_items WHERE syncStatus IN (:statuses)")
    fun observePendingSyncCount(
        statuses: List<SyncStatus> = listOf(
            SyncStatus.PENDING,
            SyncStatus.FAILED,
            SyncStatus.SYNCING
        )
    ): Flow<Int>

    @Query("DELETE FROM sync_items WHERE syncStatus = :status AND lastSyncAttempt < :cutoffTime")
    suspend fun cleanupOldSyncItems(status: SyncStatus = SyncStatus.SYNCED, cutoffTime: Long): Int
}