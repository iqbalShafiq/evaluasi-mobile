package id.usecase.assessment.presentation.screens.class_room.create.sections.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.text_field.EvaluasiTextField

@Composable
fun SubSectionCard(
    modifier: Modifier = Modifier,
    state: SubSectionState,
    descriptionIndex: Int,
    onDescriptionChanged: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.4f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (descriptionIndex + 1).toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        EvaluasiTextField(
            modifier = Modifier
                .weight(3f),
            label = "Point ${descriptionIndex + 1}",
            placeholder = "Type description point",
            value = state.description,
            onValueChange = {
                onDescriptionChanged(it)
            },
            keyboardType = KeyboardType.Text
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SubSectionCardPreview() {
    EvaluasiTheme {
        SubSectionCard(
            state = SubSectionState(),
            descriptionIndex = 0,
            onDescriptionChanged = {}
        )
    }
}