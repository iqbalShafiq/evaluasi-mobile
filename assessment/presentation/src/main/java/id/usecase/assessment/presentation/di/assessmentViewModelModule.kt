package id.usecase.assessment.presentation.di

import id.usecase.assessment.presentation.screens.assessment.AssessmentViewModel
import id.usecase.assessment.presentation.screens.class_room.create.CreateClassRoomViewModel
import id.usecase.assessment.presentation.screens.class_room.create.categories.AddCategoriesViewModel
import id.usecase.assessment.presentation.screens.class_room.create.sections.SectionEditorViewModel
import id.usecase.assessment.presentation.screens.class_room.create.students.AddStudentsViewModel
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomViewModel
import id.usecase.assessment.presentation.screens.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val assessmentViewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::CreateClassRoomViewModel)
    viewModelOf(::AddCategoriesViewModel)
    viewModelOf(::AddStudentsViewModel)
    viewModelOf(::AssessmentViewModel)
    viewModelOf(::ClassRoomViewModel)
    viewModelOf(::SectionEditorViewModel)
}