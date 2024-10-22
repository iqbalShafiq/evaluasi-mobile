package id.usecase.assessment.presentation.screens.class_room.create.students

import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentCardState

sealed class AddStudentsAction {
    data class AddStudents(val students: List<AddStudentCardState>) : AddStudentsAction()
}