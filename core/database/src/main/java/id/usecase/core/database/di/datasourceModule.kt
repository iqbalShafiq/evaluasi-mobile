package id.usecase.core.database.di

import id.usecase.core.database.RealmConfig
import id.usecase.core.database.RealmLocalAssessmentDataSource
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import io.realm.kotlin.Realm
import org.koin.dsl.module

val dataSourceModule = module {
    single<Realm> {
        RealmConfig.getRealm()
    }

    single<LocalAssessmentDataSource> {
        RealmLocalAssessmentDataSource(get())
    }
}