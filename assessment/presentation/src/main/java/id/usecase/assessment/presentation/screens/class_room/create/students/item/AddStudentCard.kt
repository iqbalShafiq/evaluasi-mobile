package id.usecase.assessment.presentation.screens.class_room.create.students.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.text_field.EvaluasiTextField

@Composable
fun AddStudentCard(
    modifier: Modifier = Modifier,
    state: AddStudentCardState
) {

    Card(
        modifier = modifier
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
                state = state.identifier,
                inputType = KeyboardType.Number,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )

            EvaluasiTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                label = "Name",
                placeholder = "Type student name",
                state = state.name,
                inputType = KeyboardType.Number,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    }
}

@Preview
@Composable
private fun AddStudentCardPreview() {
    EvaluasiTheme {
        AddStudentCard(
            state = AddStudentCardState()
        )
    }
}