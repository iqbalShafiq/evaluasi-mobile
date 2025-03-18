package id.usecase.assessment.data.di

import id.usecase.assessment.data.AnalyticsRepositoryImpl
import id.usecase.assessment.data.AssessmentRepositoryImpl
import id.usecase.assessment.data.CategoryRepositoryImpl
import id.usecase.assessment.data.ClassRoomRepositoryImpl
import id.usecase.assessment.data.EventRepositoryImpl
import id.usecase.assessment.data.SectionRepositoryImpl
import id.usecase.assessment.data.StudentRepositoryImpl
import id.usecase.assessment.domain.AnalyticsRepository
import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.assessment.domain.CategoryRepository
import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.assessment.domain.EventRepository
import id.usecase.assessment.domain.SectionRepository
import id.usecase.assessment.domain.StudentRepository
import org.koin.dsl.module

val assessmentRepositoryModule = module {
    single<ClassRoomRepository> {
        ClassRoomRepositoryImpl(
            dataSource = get(),
            syncService = get(),
            dispatcher = get(),
        )
    }

    single<CategoryRepository> {
        CategoryRepositoryImpl(
            dataSource = get(),
            syncService = get(),
            dispatcher = get(),
        )
    }

    single<EventRepository> {
        EventRepositoryImpl(
            dataSource = get(),
            syncService = get(),
            dispatcher = get(),
        )
    }

    single<AssessmentRepository> {
        AssessmentRepositoryImpl(
            dataSource = get(),
            syncService = get(),
            dispatcher = get(),
        )
    }

    single<StudentRepository> {
        StudentRepositoryImpl(
            dataSource = get(),
            syncService = get(),
            dispatcher = get(),
        )
    }

    single<AnalyticsRepository> {
        AnalyticsRepositoryImpl(
            dataSource = get(),
            dispatcher = get(),
        )
    }

    single<SectionRepository> {
        SectionRepositoryImpl(
            dataSource = get(),
            syncService = get(),
            dispatcher = get(),
        )
    }
}