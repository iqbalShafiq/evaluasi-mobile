package id.usecase.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import id.usecase.core.domain.sync.EntityType
import id.usecase.core.domain.sync.SyncStatus

@Entity(tableName = "sync_items")
data class SyncEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val entityId: Int,
    val entityType: EntityType,
    val syncStatus: SyncStatus,
    val priority: Int = 0,
    val attemptCount: Int = 0,
    val lastSyncAttempt: Long? = null,
    val failureReason: String? = null,
    val createdTime: Long = System.currentTimeMillis()
)