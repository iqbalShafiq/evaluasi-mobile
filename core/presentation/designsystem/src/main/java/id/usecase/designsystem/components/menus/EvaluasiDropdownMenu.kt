package id.usecase.designsystem.components.menus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.ActionItem
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar

data class DropdownMenuItem(
    val text: String,
    val leadingIcon: ImageVector? = null,
    val trailingText: String? = null,
    val onClick: () -> Unit
)

@Composable
fun EvaluasiDropdownMenu(
    menuItems: List<DropdownMenuItem>,
    modifier: Modifier = Modifier,
    showDividerAfter: List<Int> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItems.forEachIndexed { index, item ->
                androidx.compose.material3.DropdownMenuItem(
                    text = { Text(item.text) },
                    onClick = {
                        expanded = false
                        item.onClick()
                    },
                    leadingIcon = item.leadingIcon?.let { icon ->
                        {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = icon,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = item.trailingText?.let { text ->
                        { Text(text, textAlign = TextAlign.Center) }
                    }
                )

                if (index in showDividerAfter) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun EvaluasiDropdownMenuPreview() {
    val menuItems = listOf(
        DropdownMenuItem(
            text = "Edit",
            leadingIcon = Icons.Outlined.Edit
        ) {
            // Handle edit
        },
        DropdownMenuItem(
            text = "Settings",
            leadingIcon = Icons.Outlined.Settings
        ) {
            // Handle settings
        },
        DropdownMenuItem(
            text = "Send Feedback",
            leadingIcon = Icons.Outlined.Email,
            trailingText = "F11"
        ) {
            // Handle send feedback
        }
    )

    EvaluasiTheme {
        Scaffold(
            topBar = {
                EvaluasiTopAppBar(
                    title = "Dropdown Menu",
                    trailingIcons = listOf(
                        ActionItem(
                            icon = Icons.Default.Search,
                            contentDescription = "More",
                            onClick = {}
                        )
                    ),
                    moreMenu = {
                        EvaluasiDropdownMenu(menuItems = menuItems)
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}