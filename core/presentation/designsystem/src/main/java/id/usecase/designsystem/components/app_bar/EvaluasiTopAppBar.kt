@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.designsystem.components.app_bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.R

@Composable
fun EvaluasiTopAppBar(
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: ImageVector? = null,
    navigationIconTint: Color? = null,
    trailingIcon: ImageVector? = null,
    onNavigationClicked: () -> Unit = {},
    onTrailingIconClicked: () -> Unit = {},
    centeredTitle: Boolean = false,
    centeredSubtitle: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    title: String
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            navigationIcon?.let {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable { onNavigationClicked() },
                    imageVector = navigationIcon,
                    colorFilter = navigationIconTint?.let { ColorFilter.tint(it) },
                    contentDescription = stringResource(R.string.navigation_icon)
                )
            }
        },
        title = {
            Column(
                modifier = if (navigationIcon != null) {
                    Modifier.padding(start = 16.dp, end = 24.dp)
                } else {
                    Modifier.padding(horizontal = 4.dp)
                }
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = if (centeredTitle) TextAlign.Center else TextAlign.Start
                )

                subtitle?.let {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = if (centeredSubtitle) TextAlign.Center else TextAlign.Start
                    )
                }
            }
        },
        actions = {
            trailingIcon?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { onTrailingIconClicked() },
                    imageVector = trailingIcon,
                    contentDescription = stringResource(R.string.trailing_icon)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Preview
@Composable
private fun EvaluasiTopAppBarPreview() {
    EvaluasiTopAppBar(
        title = "Test",
        subtitle = "Subtitle!"
    )
}