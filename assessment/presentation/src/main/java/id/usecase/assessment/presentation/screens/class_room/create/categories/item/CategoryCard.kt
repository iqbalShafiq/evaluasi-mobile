package id.usecase.assessment.presentation.screens.class_room.create.categories.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
        shape = MaterialTheme.shapes.small,
        content = {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                EvaluasiTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Category Name",
                    placeholder = "Enter category name",
                    state = state.name,
                    inputType = KeyboardType.Text,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )

                EvaluasiTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    label = "Percentage",
                    placeholder = "Enter Percentage (total must be 100)",
                    state = state.partPercentage,
                    inputType = KeyboardType.Number,
                    isError = percentageExceeded,
                    errorMessage = errorMessage,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )

                EvaluasiTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    label = "Category Description",
                    placeholder = "Enter category description (optional)",
                    state = state.description,
                    inputType = KeyboardType.Text,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
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
            name = rememberTextFieldState(),
            partPercentage = rememberTextFieldState(),
            description = rememberTextFieldState(),
        ),
        percentageExceeded = true,
        errorMessage = "Total percentage must be 100"
    )
}