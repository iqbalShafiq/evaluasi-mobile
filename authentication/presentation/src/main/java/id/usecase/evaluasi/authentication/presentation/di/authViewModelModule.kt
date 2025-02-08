package id.usecase.evaluasi.authentication.presentation.di

import id.usecase.evaluasi.authentication.presentation.login.LoginViewModel
import id.usecase.evaluasi.authentication.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}