package id.usecase.core.data.di

import id.usecase.core.data.auth.EncryptedSessionStorage
import id.usecase.core.data.networking.HttpClientFactory
import id.usecase.core.domain.assessment.auth.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single { HttpClientFactory(get()).build() }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}

