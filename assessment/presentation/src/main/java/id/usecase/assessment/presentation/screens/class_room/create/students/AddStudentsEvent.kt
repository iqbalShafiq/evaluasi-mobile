package id.usecase.assessment.presentation.screens.class_room.create.students

sealed class AddStudentsEvent {
    data class OnErrorOccurred(val message: String) : AddStudentsEvent()
    object OnStudentsHasAdded : AddStudentsEvent()
}