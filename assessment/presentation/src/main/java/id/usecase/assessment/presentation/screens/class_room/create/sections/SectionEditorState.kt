package id.usecase.assessment.presentation.screens.class_room.create.sections

import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SectionCardState

data class SectionEditorState(
    val isLoading: Boolean = false,
    val classRoomId: Int = 0,
    val sectionStates: List<SectionCardState> = listOf(SectionCardState())
)