package id.usecase.assessment.presentation.screens.class_room.create.students

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import id.usecase.assessment.presentation.models.AddStudentUi
import id.usecase.assessment.presentation.utils.toUi

class AddStudentsViewModel : ViewModel() {
    var state = mutableStateOf(AddStudentsState())
        private set

    fun onAction(action: AddStudentsAction) {
        when (action) {
            is AddStudentsAction.AddStudents -> {
                val students = action.students.map {
                    it.toUi()
                }

                submitStudents(students)
            }
        }
    }

    private fun submitStudents(students: List<AddStudentUi>) {

    }
}