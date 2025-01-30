package id.usecase.assessment.presentation.screens.class_room.create.sections.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.text_field.EvaluasiTextField

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    state: SectionCardState,
    onNameChanged: (TextFieldValue) -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        val subSections = remember { mutableStateListOf<SubSectionState>() }
        subSections.clear()
        subSections.addAll(state.subSections)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            EvaluasiTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                label = "Name",
                placeholder = "Type Section name",
                value = state.name,
                onValueChange = {
                    onNameChanged(it)
                },
                keyboardType = KeyboardType.Text
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp),
                contentPadding = PaddingValues(start = 12.dp, top = 8.dp)
            ) {
                items(subSections.size) { index ->
                    val subSection = subSections[index]

                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        SubSectionCard(
                            state = subSection,
                            descriptionIndex = index,
                            onDescriptionChanged = { description ->
                                subSections[index] = subSection.copy(description = description)
                            }
                        )

                        if (
                            index == subSections.size - 1 &&
                            subSection.description.text.isNotEmpty()
                        ) subSections.add(SubSectionState())

                        if (
                            index != subSections.size - 1 &&
                            subSection.description.text.isEmpty()
                        ) subSections.removeAt(index)
                    }

                    if (index != subSections.size - 1) {
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionCardPreview() {
    EvaluasiTheme {
        SectionCard(
            state = SectionCardState(
                name = TextFieldValue("Section 1"),
                subSections = listOf(
                    SubSectionState(description = TextFieldValue("Sub Section 1")),
                    SubSectionState(description = TextFieldValue("Sub Section 2")),
                )
            ),
            onNameChanged = {}
        )
    }
}