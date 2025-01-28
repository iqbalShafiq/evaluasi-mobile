package id.usecase.designsystem.components.text_field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun EvaluasiDropdown(
    modifier: Modifier = Modifier,
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    var fieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    fieldSize = layoutCoordinates.size.toSize()
                },
            value = selectedItem,
            onValueChange = { },
            readOnly = true,
            enabled = enabled,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = MaterialTheme.colorScheme.error
            ),
            label = { Text(text = label) },
            placeholder = {
                if (placeholder != null) Text(text = placeholder)
            },
            leadingIcon = leadingIcon,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle dropdown",
                        modifier = Modifier.rotate(if (expanded) 180f else 0f)
                    )
                }
            },
            singleLine = true,
            isError = !errorMessage.isNullOrEmpty(),
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { fieldSize.width.toDp() })
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item)
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }

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