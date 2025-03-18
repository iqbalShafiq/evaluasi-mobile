@file:OptIn(ExperimentalCoroutinesApi::class)

package id.usecase.evaluasi

import android.app.Application
import androidx.work.Configuration
import id.usecase.assessment.data.di.assessmentRepositoryModule
import id.usecase.assessment.presentation.di.assessmentViewModelModule
import id.usecase.core.data.di.coreDataModule
import id.usecase.core.data.di.syncModule
import id.usecase.core.database.di.dataSourceModule
import id.usecase.core.domain.di.coroutineModule
import id.usecase.evaluasi.authentication.data.di.authDataModule
import id.usecase.evaluasi.authentication.presentation.di.authViewModelModule
import id.usecase.evaluasi.di.appModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class MyApplication : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            workManagerFactory()
            modules(
                appModule,
                dataSourceModule,
                coreDataModule,
                assessmentViewModelModule,
                assessmentRepositoryModule,
                authViewModelModule,
                authDataModule,
                coroutineModule,
                syncModule
            )
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}