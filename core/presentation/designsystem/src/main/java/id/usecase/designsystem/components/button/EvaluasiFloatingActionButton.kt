package id.usecase.designsystem.components.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.R

@Composable
fun EvaluasiFloatingActionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    iconContentDescription: String,
    onClickListener: () -> Unit
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        icon = {
            Image(
                imageVector = icon,
                contentDescription = iconContentDescription,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        onClick = { onClickListener() }
    )
}

@Preview
@Composable
private fun EvaluasiFloatingActionButtonPreview() {
    EvaluasiFloatingActionButton(
        text = "Test",
        icon = ImageVector.vectorResource(id = R.drawable.ic_test_icon),
        iconContentDescription = "Add",
        onClickListener = { /*TODO*/ }
    )
}