package id.usecase.designsystem.components.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EvaluasiTextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    readOnly: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    state: TextFieldState,
    inputType: KeyboardType,
    clickAction: Boolean = false,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    if (clickAction && isPressed) onClick()

    BasicTextField(
        modifier = if (clickAction) {
            modifier.pointerInput(Unit) {}
        } else {
            modifier
        },
        state = state,
        readOnly = readOnly || clickAction,
        interactionSource = interactionSource,
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (clickAction) KeyboardType.Text else inputType,
            imeAction = ImeAction.Done
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        decorator = { innerTextField ->
            Column {
                label?.let {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .background(
                            color = containerColor,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    innerTextField()
                    placeholder?.let {
                        if (state.text.toString().isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                ),
                            )
                        }
                    }
                }

                if (isError) {
                    errorMessage?.let {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            text = errorMessage,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun EvaluasiTextFieldPreview() {
    val state by remember {
        mutableStateOf(TextFieldState())
    }

    EvaluasiTextField(
        state = state,
        label = "Label",
        placeholder = "Placeholder",
        errorMessage = "Error message",
        isError = true,
        inputType = KeyboardType.Text
    )
}