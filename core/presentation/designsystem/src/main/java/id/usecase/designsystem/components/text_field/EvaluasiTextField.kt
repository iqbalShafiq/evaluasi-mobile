package id.usecase.designsystem.components.text_field

import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun EvaluasiTextField(
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    placeHolder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        onValueChange = onValueChange,
        label = { Text(text = label) },
        readOnly = readOnly,
        placeholder = {
            if (placeHolder != null) Text(text = placeHolder)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = singleLine && minLines == 1 && maxLines == 1,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        )
    )
}