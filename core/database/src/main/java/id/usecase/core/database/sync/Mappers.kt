package id.usecase.core.database.sync

import id.usecase.core.database.entities.SyncEntity
import id.usecase.core.domain.sync.SyncModel

fun SyncModel.toEntity(): SyncEntity {
    return SyncEntity(
        id = id,
        entityId = entityId,
        entityType = entityType,
        syncStatus = syncStatus,
        priority = priority,
        attemptCount = attemptCount,
        lastSyncAttempt = lastSyncAttempt,
        failureReason = failureReason,
        createdTime = createdTime
    )
}

fun SyncEntity.toModel(): SyncModel {
    return SyncModel(
        id = id,
        entityId = entityId,
        entityType = entityType,
        syncStatus = syncStatus,
        priority = priority,
        attemptCount = attemptCount,
        lastSyncAttempt = lastSyncAttempt,
        failureReason = failureReason,
        createdTime = createdTime
    )
}