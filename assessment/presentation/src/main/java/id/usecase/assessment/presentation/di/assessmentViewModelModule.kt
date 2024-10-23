package id.usecase.assessment.presentation.di

import id.usecase.assessment.presentation.screens.class_room.create.CreateClassRoomViewModel
import id.usecase.assessment.presentation.screens.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val assessmentViewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::CreateClassRoomViewModel)
}