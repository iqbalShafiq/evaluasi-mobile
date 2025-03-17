package id.usecase.core.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import id.usecase.core.data.sync.model.AssessmentNetworkModel
import id.usecase.core.data.utils.NetworkUtils
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.sync.EntityType
import id.usecase.core.domain.sync.SyncStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Worker that performs data synchronization in the background
 */
class SyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val apiService: SyncApiService by inject()
    private val syncRepository: SyncRepository by inject()
    private val notificationService: NotificationService by inject()
    private val entityFactory: EntitySyncFactory by inject()
    private val assessmentDataSource: LocalAssessmentDataSource by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            if (!NetworkUtils.isNetworkConnected(context)) {
                return@withContext Result.retry()
            }

            // Get pending items to sync (max 50 at a time)
            val itemsToSync = syncRepository.getPendingSyncItems(50)

            if (itemsToSync.isEmpty()) {
                return@withContext Result.success()
            }

            // Show notification
            notificationService.showSyncNotification(true, itemsToSync.size)

            var successCount = 0
            var failCount = 0

            // Process each item
            for (syncItem in itemsToSync) {
                try {
                    // Get the actual entity from database
                    val entity = when (syncItem.entityType) {
                        EntityType.CLASS_ROOM -> assessmentDataSource.getClassRoomById(syncItem.entityId)
                        EntityType.STUDENT -> assessmentDataSource.getStudentById(syncItem.entityId)
                        EntityType.CATEGORY -> assessmentDataSource.getCategoryById(syncItem.entityId)
                        EntityType.SECTION -> assessmentDataSource.getSectionById(syncItem.entityId)
                        EntityType.EVENT -> assessmentDataSource.getEventById(syncItem.entityId)
                        EntityType.EVENT_SECTION -> {
                            // Decompose the composite key
                            val eventId = syncItem.entityId / 31
                            val sectionId = syncItem.entityId % 31
                            assessmentDataSource.getEventSectionCrossRef(eventId, sectionId)
                        }
                        EntityType.ASSESSMENT -> assessmentDataSource.getAssessmentById(syncItem.entityId)
                    }

                    if (entity == null) {
                        // Entity was deleted, mark sync item as synced
                        syncRepository.updateSyncResult(syncItem.id, true)
                        successCount++
                        continue
                    }

                    // Convert entity to network model
                    val syncableEntity = entityFactory.createSyncableEntity(syncItem.entityType, entity)
                    val networkModel = syncableEntity.toNetworkModel()

                    // Sync to server using Ktor
                    val result = when (syncItem.entityType) {
                        EntityType.CLASS_ROOM -> apiService.syncClassRoom(networkModel)
                        EntityType.STUDENT -> apiService.syncStudent(networkModel)
                        EntityType.CATEGORY -> apiService.syncCategory(networkModel)
                        EntityType.SECTION -> apiService.syncSection(networkModel)
                        EntityType.EVENT -> apiService.syncEvent(networkModel)
                        EntityType.EVENT_SECTION -> apiService.syncEventSection(networkModel)
                        EntityType.ASSESSMENT -> apiService.syncAssessment(networkModel as AssessmentNetworkModel)
                    }

                    // Update sync result
                    when (result) {
                        is Result.Success -> {
                            syncRepository.updateSyncResult(syncItem.id, true)
                            successCount++
                        }
                        is Result.Error -> {
                            val errorMsg = result.error.message
                            syncRepository.updateSyncResult(syncItem.id, false, errorMsg)
                            failCount++
                        }
                    }

                } catch (e: Exception) {
                    Timber.e(e, "Error syncing item ${syncItem.id}")
                    syncRepository.updateSyncResult(syncItem.id, false, e.message)
                    failCount++
                }
            }

            // Cleanup old sync records
            syncRepository.cleanupOldSyncRecords()

            // Show completion notification
            if (failCount > 0) {
                notificationService.showSyncCompletionNotification(successCount, failCount)
            } else {
                notificationService.cancelSyncNotification()
            }

            // If there are still items to sync, schedule another sync
            if (syncRepository.getPendingSyncItems(1).isNotEmpty()) {
                syncRepository.scheduleImmediateSync()
            }

            androidx.work.ListenableWorker.Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Sync worker failed")
            notificationService.showSyncFailureNotification(e.message ?: "Unknown error")
            androidx.work.ListenableWorker.Result.failure()
        }
    }
}