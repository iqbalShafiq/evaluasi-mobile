package id.usecase.assessment.presentation.screens.class_room.create.sections.components

import androidx.compose.ui.text.input.TextFieldValue

data class SectionCardState(
    val sectionId: Int = 0,
    val name: TextFieldValue = TextFieldValue(),
    val subSections: List<SubSectionState> = emptyList(),
    val isExpanded: Boolean = false
)
