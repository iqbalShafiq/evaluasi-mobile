package id.usecase.evaluasi.authentication.data.di

import id.usecase.evaluasi.authentication.data.AuthRepositoryImpl
import id.usecase.evaluasi.authentication.data.EmailPatternValidator
import id.usecase.evaluasi.authentication.domain.AuthRepository
import id.usecase.evaluasi.authentication.domain.PatternValidator
import id.usecase.evaluasi.authentication.domain.UserDataValidator
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> { EmailPatternValidator }
    single<UserDataValidator> { UserDataValidator(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}