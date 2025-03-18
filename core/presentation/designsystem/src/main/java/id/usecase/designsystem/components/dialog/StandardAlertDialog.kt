package id.usecase.designsystem.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StandardAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmationText: String = "Confirm",
    dismissText: String = "Dismiss"
) {
    AlertDialog(
        title = {
            Text(
                text = dialogTitle,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = dialogText,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        onDismissRequest = {
            onDismissRequest()
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
    )
}

@Preview
@Composable
private fun AlertDialogPreview() {
    StandardAlertDialog(
        onDismissRequest = {},
        onConfirmation = {},
        dialogTitle = "Title",
        dialogText = "Text"
    )
}