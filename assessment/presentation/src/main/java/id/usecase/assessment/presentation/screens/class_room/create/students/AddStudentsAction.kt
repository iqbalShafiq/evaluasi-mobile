package id.usecase.assessment.presentation.screens.class_room.create.students

import id.usecase.assessment.presentation.screens.class_room.create.students.components.AddStudentItemState

sealed class AddStudentsAction {
    data class LoadStudents(val classRoomId: String) : AddStudentsAction()
    data class AddStudents(
        val students: List<AddStudentItemState>,
        val classRoomId: String
    ) : AddStudentsAction()
}