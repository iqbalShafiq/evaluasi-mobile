package id.usecase.assessment.presentation.screens.class_room.create.categories.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    errorMessage: String = ""
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        content = {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                EvaluasiTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Category Name",
                    placeholder = "Enter category name",
                    value = state.name,
                    onValueChange = { },
                    keyboardType = KeyboardType.Text,
                )

                EvaluasiTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    label = "Percentage",
                    placeholder = "Enter Percentage (total must be 100)",
                    value = state.partPercentage,
                    onValueChange = {},
                    keyboardType = KeyboardType.Number
                )

                EvaluasiTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    label = "Category Description",
                    placeholder = "Enter category description (optional)",
                    value = state.description,
                    onValueChange = {},
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
        errorMessage = "Total percentage must be 100"
    )
}