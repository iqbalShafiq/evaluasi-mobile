package id.usecase.evaluasi

import android.app.Application
import id.usecase.assessment.data.di.assessmentRepositoryModule
import id.usecase.core.database.di.dataSourceModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            dataSourceModule
            assessmentRepositoryModule
        }

        startKoin {
            modules(appModule)
        }
    }
}