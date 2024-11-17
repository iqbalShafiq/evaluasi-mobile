package id.usecase.designsystem.components.app_bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.R

@Composable
fun EvaluasiBottomAppBar(
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClicked: () -> Unit = {},
    actionItemList: List<ActionItem> = emptyList(),
) {
    BottomAppBar(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigationClicked,
                content = {
                    navigationIcon?.let {
                        Image(
                            imageVector = navigationIcon,
                            contentDescription = "Navigation Icon",
                            colorFilter = ColorFilter.tint(
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            )
        },
        actions = {
            actionItemList.forEach { actionItem ->
                IconButton(
                    onClick = { actionItem.onClick() },
                    content = {
                        Icon(
                            imageVector = actionItem.icon,
                            contentDescription = actionItem.contentDescription
                        )
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun EvaluasiBottomAppBarPreview() {
    EvaluasiTheme {
        Scaffold(
            bottomBar = {
                EvaluasiBottomAppBar(
                    navigationIcon = ImageVector.vectorResource(R.drawable.ic_test_icon),
                    onNavigationClicked = { },
                    actionItemList = listOf(
                        ActionItem(
                            icon = ImageVector.vectorResource(R.drawable.ic_test_icon),
                            contentDescription = "Test Icon",
                            onClick = { }
                        ),
                        ActionItem(
                            icon = ImageVector.vectorResource(R.drawable.ic_test_icon),
                            contentDescription = "Test Icon",
                            onClick = { }
                        ),
                        ActionItem(
                            icon = ImageVector.vectorResource(R.drawable.ic_test_icon),
                            contentDescription = "Test Icon",
                            onClick = { }
                        )
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) { }
        }
    }
}