package id.usecase.assessment.presentation.screens.class_room.create.sections

sealed class SectionEditorEvent {
    data class OnErrorOccurred(val message: String) : SectionEditorEvent()
    data object OnSaveSuccess : SectionEditorEvent()
}