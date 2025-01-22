package id.usecase.assessment.presentation.screens.class_room.create.categories.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.components.text_field.EvaluasiTextField

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    state: CategoryItemState,
    percentageExceeded: Boolean = false,
    errorMessage: String = "",
    onNameChanged: (TextFieldValue) -> Unit,
    onPercentageChanged: (TextFieldValue) -> Unit,
    onDescriptionChanged: (TextFieldValue) -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                EvaluasiTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Category Name",
                    placeholder = "Enter category name",
                    value = state.name,
                    onValueChange = { onNameChanged(it) },
                    keyboardType = KeyboardType.Text,
                )

                EvaluasiTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    label = "Percentage",
                    placeholder = "Enter Percentage (total must be 100)",
                    value = state.partPercentage,
                    onValueChange = { onPercentageChanged(it) },
                    keyboardType = KeyboardType.Number,
                    errorMessage = if (percentageExceeded) errorMessage else "",
                )

                EvaluasiTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    label = "Category Description",
                    placeholder = "Enter category description (optional)",
                    value = state.description,
                    onValueChange = { onDescriptionChanged(it) },
                    keyboardType = KeyboardType.Text,
                    minLines = 3,
                    maxLines = 3
                )
            }
        }
    )
}

@Preview
@Composable
private fun CategoryCardPreview() {
    CategoryCard(
        state = CategoryItemState(
            name = TextFieldValue(),
            partPercentage = TextFieldValue(),
            description = TextFieldValue(),
        ),
        percentageExceeded = true,
        errorMessage = "Total percentage must be 100",
        onNameChanged = {},
        onPercentageChanged = {},
        onDescriptionChanged = {}
    )
}