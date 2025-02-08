@file:OptIn(ExperimentalCoroutinesApi::class)

package id.usecase.evaluasi

import android.app.Application
import id.usecase.assessment.data.di.assessmentRepositoryModule
import id.usecase.assessment.presentation.di.assessmentViewModelModule
import id.usecase.core.data.di.coreDataModule
import id.usecase.core.database.di.dataSourceModule
import id.usecase.core.domain.di.coroutineModule
import id.usecase.evaluasi.authentication.data.di.authDataModule
import id.usecase.evaluasi.authentication.presentation.di.authViewModelModule
import id.usecase.evaluasi.di.appModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                appModule,
                dataSourceModule,
                coreDataModule,
                assessmentViewModelModule,
                assessmentRepositoryModule,
                authViewModelModule,
                authDataModule,
                coroutineModule
            )
        }
    }
}