@file:OptIn(ExperimentalCoroutinesApi::class)

package id.usecase.evaluasi

import android.app.Application
import id.usecase.assessment.data.di.assessmentRepositoryModule
import id.usecase.assessment.presentation.di.assessmentViewModelModule
import id.usecase.core.database.di.dataSourceModule
import id.usecase.core.domain.assessment.di.coroutineModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                dataSourceModule,
                assessmentViewModelModule,
                assessmentRepositoryModule,
                coroutineModule
            )
        }
    }
}