package id.usecase.core.data.di

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import id.usecase.core.data.sync.EntitySyncFactory
import id.usecase.core.data.sync.NotificationService
import id.usecase.core.data.sync.SyncApiService
import id.usecase.core.data.sync.SyncService
import id.usecase.core.data.sync.SyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val syncModule = module {
    single { EntitySyncFactory() }
    single { SyncApiService(get()) }
    single { SyncService(get(), get()) }
    single { NotificationService(androidContext()) }
    single { WorkManager.getInstance(androidContext()) }
    single { PeriodicSyncScheduler(get(), get()) }
}



class PeriodicSyncScheduler(
    private val workManager: WorkManager,
    private val context: Context
) {
    fun schedule() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "periodic_sync_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            syncWorkRequest
        )
    }
}