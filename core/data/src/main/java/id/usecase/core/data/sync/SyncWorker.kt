package id.usecase.core.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import id.usecase.core.data.sync.model.AssessmentNetworkModel
import id.usecase.core.data.sync.model.CategoryNetworkModel
import id.usecase.core.data.sync.model.ClassRoomNetworkModel
import id.usecase.core.data.sync.model.EventNetworkModel
import id.usecase.core.data.sync.model.EventSectionNetworkModel
import id.usecase.core.data.sync.model.SectionNetworkModel
import id.usecase.core.data.sync.model.StudentNetworkModel
import id.usecase.core.data.utils.NetworkUtils
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.sync.EntityType
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
    private val syncService: SyncService by inject()
    private val notificationService: NotificationService by inject()
    private val entityFactory: EntitySyncFactory by inject()
    private val assessmentDataSource: LocalAssessmentDataSource by inject()

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            if (!NetworkUtils.isNetworkConnected(context)) {
                return@withContext Result.retry()
            }

            // Get pending items to sync (max 50 at a time)
            val itemsToSync = syncService.getPendingSyncItems(50)

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
                        EntityType.CLASS_ROOM -> assessmentDataSource.getClassRoomById(
                            classRoomId = syncItem.entityId
                        )

                        EntityType.STUDENT -> assessmentDataSource.getStudentById(
                            studentId = syncItem.entityId
                        )

                        EntityType.CATEGORY -> assessmentDataSource.getCategoryById(
                            categoryId = syncItem.entityId
                        )

                        EntityType.SECTION -> assessmentDataSource.getSectionById(
                            sectionId = syncItem.entityId
                        )

                        EntityType.EVENT -> assessmentDataSource.getEventById(
                            eventId = syncItem.entityId
                        )

                        EntityType.EVENT_SECTION -> assessmentDataSource.getEventSectionCrossRef(
                            eventSectionId = syncItem.entityId
                        )

                        EntityType.ASSESSMENT -> assessmentDataSource.getAssessmentById(
                            assessmentId = syncItem.entityId
                        )
                    }

                    if (entity == null) {
                        // Entity was deleted, mark sync item as synced
                        syncService.updateSyncResult(syncItem.id, true)
                        successCount++
                        continue
                    }

                    // Convert entity to network model
                    val syncableEntity = entityFactory.createSyncableEntity(
                        entityType = syncItem.entityType,
                        entity = entity
                    )
                    val networkModel = syncableEntity.toNetworkModel()

                    // Sync to server using Ktor
                    val result = when (syncItem.entityType) {
                        EntityType.CLASS_ROOM -> apiService.syncClassRoom(networkModel as ClassRoomNetworkModel)
                        EntityType.STUDENT -> apiService.syncStudent(networkModel as StudentNetworkModel)
                        EntityType.CATEGORY -> apiService.syncCategory(networkModel as CategoryNetworkModel)
                        EntityType.SECTION -> apiService.syncSection(networkModel as SectionNetworkModel)
                        EntityType.EVENT -> apiService.syncEvent(networkModel as EventNetworkModel)
                        EntityType.EVENT_SECTION -> apiService.syncEventSection(networkModel as EventSectionNetworkModel)
                        EntityType.ASSESSMENT -> apiService.syncAssessment(networkModel as AssessmentNetworkModel)
                    }

                    // Update sync result
                    when (result) {
                        is id.usecase.core.domain.utils.Result.Success -> {
                            syncService.updateSyncResult(syncItem.id, true)
                            successCount++
                        }

                        is id.usecase.core.domain.utils.Result.Error -> {
                            val errorMsg = result.error.message
                            syncService.updateSyncResult(syncItem.id, false, errorMsg)
                            failCount++
                        }
                    }
                } catch (e: Exception) {
                    syncService.updateSyncResult(syncItem.id, false, e.message)
                    failCount++
                }
            }

            // Cleanup old sync records
            syncService.cleanupOldSyncRecords()

            // Show completion notification
            if (failCount > 0) {
                notificationService.showSyncNotification(false, successCount)
            } else {
                notificationService.cancelSyncNotification()
            }

            // If there are still items to sync, schedule another sync
            if (syncService.getPendingSyncItems(1).isNotEmpty()) {
                syncService.scheduleImmediateSync()
            }

            Result.success()
        } catch (e: Exception) {
            notificationService.showSyncNotification(false, 0)
            Result.failure()
        }
    }
}