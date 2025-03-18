package id.usecase.core.database.di

import androidx.room.Room
import id.usecase.core.database.RoomAppDatabase
import id.usecase.core.database.RoomLocalAssessmentDataSource
import id.usecase.core.database.sync.RoomSyncDataSource
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.sync.SyncDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<RoomAppDatabase> {
        Room.databaseBuilder(get(), RoomAppDatabase::class.java, "evaluasi")
            .build()
    }
    single { get<RoomAppDatabase>().eventDao() }
    single { get<RoomAppDatabase>().assessmentDao() }
    single { get<RoomAppDatabase>().studentDao() }
    single { get<RoomAppDatabase>().categoryDao() }
    single { get<RoomAppDatabase>().classRoomDao() }
    single { get<RoomAppDatabase>().analyticsDao() }
    single { get<RoomAppDatabase>().sectionDao() }
    single { get<RoomAppDatabase>().syncDao() }
    single<LocalAssessmentDataSource> {
        RoomLocalAssessmentDataSource(
            classRoomDao = get(),
            categoryDao = get(),
            eventDao = get(),
            assessmentDao = get(),
            studentDao = get(),
            analyticsDao = get(),
            sectionDao = get(),
            dispatcher = get()
        )
    }
    single<SyncDataSource> {
        RoomSyncDataSource(
            syncDao = get()
        )
    }
}