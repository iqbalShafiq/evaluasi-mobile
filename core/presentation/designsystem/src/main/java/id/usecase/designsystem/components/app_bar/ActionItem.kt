package id.usecase.designsystem.components.app_bar

import androidx.compose.ui.graphics.vector.ImageVector

data class ActionItem(
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit
)
