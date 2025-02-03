package id.usecase.designsystem.components.text_field

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.R

@Composable
fun EvaluasiMultipleChoiceDropdown(
    modifier: Modifier = Modifier,
    label: String,
    items: List<String>,
    selectedItems: List<String>,
    onItemsSelected: (List<String>) -> Unit,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingContentForItem: (@Composable (String) -> Unit)? = null,
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
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (enabled) expanded = !expanded
                        }
                    )
                },
            value = if (selectedItems.isEmpty()) "" else selectedItems.joinToString(", "),
            onValueChange = { },
            readOnly = true,
            enabled = false,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Checkbox indicator
                                if (item in selectedItems) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = item)
                            }
                            // Trailing content slot
                            trailingContentForItem?.invoke(item)
                        }
                    },
                    onClick = {
                        val newSelectedItems = selectedItems.toMutableList()
                        if (item in selectedItems) {
                            newSelectedItems.remove(item)
                        } else {
                            newSelectedItems.add(item)
                        }
                        onItemsSelected(newSelectedItems)
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

@Preview(showBackground = true)
@Composable
private fun EvaluasiMultipleChoiceDropdownPreview() {
    var selectedItems by remember { mutableStateOf(listOf<String>()) }
    val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")

    EvaluasiTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            EvaluasiMultipleChoiceDropdown(
                label = "Select Options",
                items = items,
                selectedItems = selectedItems,
                onItemsSelected = { selectedItems = it },
                trailingContentForItem = { item ->
                    // Optional: Add custom trailing content for each item
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_test_icon),
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}