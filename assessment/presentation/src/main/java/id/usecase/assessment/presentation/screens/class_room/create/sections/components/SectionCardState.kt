package id.usecase.assessment.presentation.screens.class_room.create.sections.components

import androidx.compose.ui.text.input.TextFieldValue

data class SectionCardState(
    val sectionId: String = "",
    val name: TextFieldValue = TextFieldValue(),
    val subSections: List<SubSectionState> = listOf(SubSectionState()),
    val isExpanded: Boolean = false,
    val isValid: Boolean = false
)
