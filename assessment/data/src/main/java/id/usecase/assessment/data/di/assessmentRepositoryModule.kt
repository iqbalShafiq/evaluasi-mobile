package id.usecase.assessment.data.di

import id.usecase.assessment.data.AssessmentRepositoryImpl
import id.usecase.assessment.domain.AssessmentRepository
import org.koin.dsl.module

val assessmentRepositoryModule = module {
    single<AssessmentRepository> {
        AssessmentRepositoryImpl(
            assessmentDataSource = get(),
            dispatcher = get(),
        )
    }
}