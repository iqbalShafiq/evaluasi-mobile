package id.usecase.assessment.presentation.screens.class_room.create.sections

import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SectionCardState
import id.usecase.core.domain.assessment.model.section.Section

data class SectionEditorState(
    val isLoading: Boolean = false,
    val classRoomId: Int = 0,
    val sections: List<Section> = emptyList(),
    val sectionStates: List<SectionCardState> = emptyList()
)