package id.usecase.assessment.presentation.screens.class_room.create.students.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.text_field.EvaluasiTextField

@Composable
fun AddStudentCard(
    modifier: Modifier = Modifier,
    state: AddStudentItemState,
    onIdentifierChanged: (TextFieldValue) -> Unit,
    onNameChanged: (TextFieldValue) -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            EvaluasiTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Identifier",
                placeholder = "Type identifier (optional)",
                value = state.identifier,
                onValueChange = {
                    onIdentifierChanged(it)
                },
                keyboardType = KeyboardType.Number
            )

            EvaluasiTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                label = "Name",
                placeholder = "Type student name",
                value = state.name,
                onValueChange = {
                    onNameChanged(it)
                },
                keyboardType = KeyboardType.Text
            )
        }
    }
}

@Preview
@Composable
private fun AddStudentCardPreview() {
    EvaluasiTheme {
        AddStudentCard(
            state = AddStudentItemState(isValid = true),
            onIdentifierChanged = {},
            onNameChanged = {}
        )
    }
}