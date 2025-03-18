package id.usecase.core.domain.sync

data class SyncModel(
    val id: Int = 0,
    val entityId: String,
    val entityType: EntityType,
    val syncStatus: SyncStatus,
    val priority: Int = 0,
    val attemptCount: Int = 0,
    val lastSyncAttempt: Long? = null,
    val failureReason: String? = null,
    val createdTime: Long = System.currentTimeMillis()
)