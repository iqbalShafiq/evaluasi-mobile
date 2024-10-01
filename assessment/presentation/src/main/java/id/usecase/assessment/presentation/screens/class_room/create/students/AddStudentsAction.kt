package id.usecase.assessment.presentation.screens.class_room.create.students

sealed class AddStudentsAction {
    data class AddStudents(val students: List<AddStudentCardState>) : AddStudentsAction()
}