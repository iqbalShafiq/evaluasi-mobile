package id.usecase.assessment.presentation.screens.class_room.create.students

import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentItemState

sealed class AddStudentsAction {
    data class LoadStudents(val classRoomId: Int) : AddStudentsAction()
    data class AddStudents(
        val students: List<AddStudentItemState>,
        val classRoomId: Int
    ) : AddStudentsAction()
}