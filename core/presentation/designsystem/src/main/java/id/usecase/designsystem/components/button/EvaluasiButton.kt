package id.usecase.designsystem.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EvaluasiButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    buttonType: ButtonType = ButtonType.SURFACE
) {
    val buttonColor = when (buttonType) {
        ButtonType.SURFACE -> MaterialTheme.colorScheme.surface
        ButtonType.INVERSE -> MaterialTheme.colorScheme.inverseSurface
        ButtonType.PRIMARY -> MaterialTheme.colorScheme.primary
    }

    val contentColor = when (buttonType) {
        ButtonType.SURFACE -> MaterialTheme.colorScheme.onSurface
        ButtonType.INVERSE -> MaterialTheme.colorScheme.inverseOnSurface
        ButtonType.PRIMARY -> MaterialTheme.colorScheme.onPrimary
    }

    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = buttonColor,
            contentColor = contentColor
        ),
        enabled = enabled
    ) {
        Text(
            text = text
        )
    }
}

@Preview
@Composable
private fun EvaluasiButtonPreview() {
    EvaluasiButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Test",
        onClick = { /*TODO*/ }
    )
}