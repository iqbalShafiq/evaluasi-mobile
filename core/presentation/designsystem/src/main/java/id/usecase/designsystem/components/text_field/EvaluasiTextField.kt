package id.usecase.designsystem.components.text_field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun EvaluasiTextField(
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1,
    errorMessage: String? = null,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = MaterialTheme.colorScheme.error
            ),
            onValueChange = onValueChange,
            label = { Text(text = label) },
            readOnly = readOnly,
            placeholder = {
                if (placeholder != null) Text(text = placeholder)
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine && minLines == 1 && maxLines == 1,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            isError = !errorMessage.isNullOrEmpty()
        )
        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}