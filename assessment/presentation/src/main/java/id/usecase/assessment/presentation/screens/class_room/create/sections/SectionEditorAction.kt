package id.usecase.assessment.presentation.screens.class_room.create.sections

sealed class SectionEditorAction {
    data object Save : SectionEditorAction()
    data class LoadSections(val classRoomId: Int) : SectionEditorAction()
}