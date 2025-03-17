package id.usecase.core.data.sync

import android.content.Context
import androidx.work.*
import id.usecase.core.data.utils.NetworkUtils
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.section.Section
import id.usecase.core.domain.assessment.model.student.Student
import id.usecase.core.domain.sync.EntityDependencyManager
import id.usecase.core.domain.sync.EntityType
import id.usecase.core.domain.sync.SyncDataSource
import id.usecase.core.domain.sync.SyncModel
import id.usecase.core.domain.sync.SyncStatus
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class SyncRepository(
    private val context: Context,
    private val syncDataSource: SyncDataSource,
    private val localAssessmentDataSource: LocalAssessmentDataSource
) {
    /**
     * Mark a single entity for sync
     */
    suspend fun <T : Any> markForSync(entity: T, entityType: EntityType) {
        val entityId = when (entity) {
            is ClassRoom -> entity.id
            is Student -> entity.id
            is Category -> entity.id
            is Section -> entity.id
            is Event -> entity.id
            is Assessment -> entity.id
            else -> throw IllegalArgumentException("Unknown entity type: ${entity::class.java.simpleName}")
        }

        val existingSync = syncDataSource.getSyncItemForEntity(entityId, entityType)
        if (existingSync != null && existingSync.syncStatus == SyncStatus.SYNCED) {
            return // Already synced successfully
        }

        val priority = EntityDependencyManager.getPriority(entityType)

        val syncEntity = SyncModel(
            entityId = entityId,
            entityType = entityType,
            syncStatus = SyncStatus.PENDING,
            priority = priority,
            createdTime = System.currentTimeMillis()
        )

        syncDataSource.insertSyncItem(syncEntity)

        // Mark dependencies for sync too (si applicable)
        markDependenciesForSync(entityType, entity)

        // If on WiFi, trigger immediate sync
        if (NetworkUtils.isWifiConnected(context)) {
            scheduleImmediateSync()
        }
    }

    /**
     * Mark multiple entities of same type for sync
     */
    suspend fun <T : Any> markMultipleForSync(entities: List<T>, entityType: EntityType) {
        if (entities.isEmpty()) return

        val priority = EntityDependencyManager.getPriority(entityType)
        val syncEntities = entities.map { entity ->
            val entityId = when (entity) {
                is ClassRoom -> entity.id
                is Student -> entity.id
                is Category -> entity.id
                is Section -> entity.id
                is Event -> entity.id
                is Assessment -> entity.id
                else -> throw IllegalArgumentException("Unknown entity type: ${entity::class.java.simpleName}")
            }

            SyncModel(
                entityId = entityId,
                entityType = entityType,
                syncStatus = SyncStatus.PENDING,
                priority = priority,
                createdTime = System.currentTimeMillis()
            )
        }

        syncDataSource.insertSyncItems(syncEntities)

        // Mark dependencies for each entity
        entities.forEach { entity ->
            markDependenciesForSync(entityType, entity)
        }

        // If on WiFi, trigger immediate sync
        if (NetworkUtils.isWifiConnected(context)) {
            scheduleImmediateSync()
        }
    }

    /**
     * Mark dependencies for sync (ex: if Assessment depends on Student & Event)
     */
    private suspend fun <T : Any> markDependenciesForSync(entityType: EntityType, entity: T) {
        when (entityType) {
            EntityType.ASSESSMENT -> {
                val assessment = entity as Assessment
                // Here you would get Student & Event entities and mark them for sync
                // For example:
                // studentRepository.getStudentById(assessment.studentId)?.let {
                //     markForSync(it, EntityType.STUDENT)
                // }
            }

            EntityType.EVENT -> {
                val event = entity as Event
                // Hereø you would get Category entity and mark it for sync
            }
            // And so on for other entity types that have dependencies
            else -> { /* No dependencies or no special handling needed */
            }
        }
    }

    /**
     * Schedule immediate sync
     */
    fun scheduleImmediateSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWork = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10, // initial delay
                TimeUnit.SECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "immediate_sync_work",
            ExistingWorkPolicy.REPLACE,
            syncWork
        )
    }

    /**
     * Get all items that need to be synchronized
     */
    suspend fun getPendingSyncItems(limit: Int = 50): List<SyncModel> {
        return syncDataSource.getPendingSyncItems(limit = limit)
    }

    /**
     * Update sync status
     */
    suspend fun updateSyncResult(id: Int, isSuccess: Boolean, errorMessage: String? = null) {
        val newStatus = if (isSuccess) SyncStatus.SYNCED else SyncStatus.FAILED
        syncDataSource.updateSyncResult(
            id = id,
            newStatus = newStatus,
            timestamp = System.currentTimeMillis(),
            reason = errorMessage
        )
    }

    /**
     * Observe pending sync count
     */
    fun observePendingSyncCount(): Flow<Int> {
        return syncDataSource.observePendingSyncCount()
    }

    /**
     * Cleanup old sync records
     */
    suspend fun cleanupOldSyncRecords(olderThan: Long = 7 * 24 * 60 * 60 * 1000L /* 7 days */) {
        val cutoffTime = System.currentTimeMillis() - olderThan
        val deletedCount = syncDataSource.cleanupOldSyncItems(cutoffTime = cutoffTime)
    }
}