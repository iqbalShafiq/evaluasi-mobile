package id.usecase.designsystem.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import id.usecase.designsystem.R

@Composable
fun EvaluasiAlertDialog(
    showDialog: Boolean,
    onDismissRequest: (() -> Unit)? = null,
    onConfirmation: () -> Unit,
    title: String,
    message: String,
    icon: ImageVector = ImageVector.vectorResource(R.drawable.ic_test_icon),
    iconDescription: String = "I letter with circle background",
    confirmationText: String = "Confirm",
    dismissText: String = "Dismiss"
) {
    if (showDialog) {
        AlertDialog(
            icon = {
                Icon(icon, contentDescription = iconDescription)
            },
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            onDismissRequest = {
                onDismissRequest?.invoke()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(
                        text = confirmationText,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                if (onDismissRequest != null) {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = dismissText,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun EvaluasiAlertDialogPreview() {
    EvaluasiAlertDialog(
        showDialog = true,
        onDismissRequest = {},
        onConfirmation = {},
        title = "Title",
        message = "Text",
        icon = ImageVector.vectorResource(id = 0),
        iconDescription = "Icon Description"
    )
}