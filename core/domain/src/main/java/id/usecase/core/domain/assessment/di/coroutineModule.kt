package id.usecase.core.domain.assessment.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val coroutineModule = module {
    single<CoroutineDispatcher> {
        Dispatchers.IO.limitedParallelism(1)
    }
}