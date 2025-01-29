package id.usecase.assessment.presentation.screens.class_room.create.students

import id.usecase.assessment.presentation.screens.class_room.create.students.components.AddStudentItemState

data class AddStudentsState(
    val isLoading: Boolean = false,
    val studentList: List<AddStudentItemState> = listOf(AddStudentItemState())
)
