package id.usecase.assessment.presentation.screens.class_room.create.students

import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentCardState

data class AddStudentsState(
    val studentList: List<AddStudentCardState> = listOf(AddStudentCardState()),
    val isLoading: Boolean = false
)
