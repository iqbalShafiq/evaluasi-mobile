package id.usecase.assessment.presentation.screens.class_room.create.sections

import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SectionCardState

sealed class SectionEditorAction {
    data class Save(
        val sections: List<SectionCardState>,
        val classRoomId: Int
    ) : SectionEditorAction()
    data class LoadSections(val classRoomId: Int) : SectionEditorAction()
}