package id.usecase.assessment.data.di

import id.usecase.assessment.data.AssessmentRepositoryImpl
import id.usecase.assessment.data.ClassRoomRepositoryImpl
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.ClassRoomRepository
import org.koin.dsl.module

val assessmentRepositoryModule = module {
    single<AssessmentRepository> {
        AssessmentRepositoryImpl(
            dataSource = get(),
            dispatcher = get(),
        )
    }

    single<ClassRoomRepository> {
        ClassRoomRepositoryImpl(
            dataSource = get(),
            dispatcher = get(),
        )
    }
}