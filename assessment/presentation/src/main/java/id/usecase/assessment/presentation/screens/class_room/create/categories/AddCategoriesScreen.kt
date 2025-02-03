@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create.categories

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.categories.components.CategoryCard
import id.usecase.assessment.presentation.screens.class_room.create.categories.components.CategoryItemState
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.ActionItem
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.dialog.StandardLoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCategoriesScreenRoot(
    modifier: Modifier = Modifier,
    classRoomId: Int,
    onBackPressed: () -> Unit,
    onCategoriesHasCreated: () -> Unit,
    viewModel: AddCategoriesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val openAlertDialog = remember { mutableStateOf(false) }
    val openLoadingDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    ObserveAsEvents(
        flow = viewModel.events,
        onEvent = { event ->
            when (event) {
                is AddCategoriesEvent.OnErrorOccurred -> {
                    openAlertDialog.value = true
                    errorMessage.value = event.message
                }

                AddCategoriesEvent.OnCategoriesHasAdded -> {
                    openLoadingDialog.value = false
                    onCategoriesHasCreated()
                }
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(
            AddCategoriesAction.LoadCategories(
                classRoomId = classRoomId
            )
        )
    }

    if (openAlertDialog.value) {
        StandardAlertDialog(
            onDismissRequest = {
                openAlertDialog.value = false
            },
            onConfirmation = {
                openAlertDialog.value = false
            },
            dialogTitle = "Error",
            dialogText = errorMessage.value,
            icon = ImageVector.vectorResource(id.usecase.designsystem.R.drawable.ic_test_icon),
            iconDescription = "Error icon"
        )
    }

    if (openLoadingDialog.value) {
        StandardLoadingDialog()
    }

    AddCategoriesScreen(
        modifier = modifier,
        classRoomId = classRoomId,
        state = state,
        onBackPressed = onBackPressed,
        onClearStudentPressed = {},
        onAction = { action ->
            viewModel.onAction(action = action)
        }
    )
}

@Composable
fun AddCategoriesScreen(
    modifier: Modifier = Modifier,
    classRoomId: Int,
    state: AddCategoriesState,
    onBackPressed: () -> Unit,
    onClearStudentPressed: () -> Unit,
    onAction: (AddCategoriesAction) -> Unit
) {
    val categories = remember { mutableStateListOf<CategoryItemState>() }

    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                title = "Add Categories",
                navigationIcon = ImageVector.vectorResource(
                    id = R.drawable.ic_rounded_arrow_back
                ),
                navigationIconTint = MaterialTheme.colorScheme.onSurface,
                trailingIcons = listOf(
                    ActionItem(
                        icon = Icons.Rounded.Clear,
                        contentDescription = "Cross X",
                        onClick = onClearStudentPressed
                    ),
                ),
                onNavigationClicked = onBackPressed,
            )
        },
        content = { innerPadding ->
            categories.clear()
            categories.addAll(state.categoryFields)
            Log.d("TAG", "AddCategoriesScreen: $categories")

            ConstraintLayout(
                modifier = modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                val (content, buttonContainer) = createRefs()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(content) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(buttonContainer.top)
                            height = Dimension.fillToConstraints
                        }
                ) {
                    Text(
                        text = "Category List",
                        style = MaterialTheme.typography.titleSmall,
                    )

                    Text(
                        text = "${categories.sumOf { it.partPercentage.text.toDoubleOrNull() ?: 0.0 }}/100 Assessments Percentage",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    LazyColumn {
                        items(categories.size) { index ->
                            val category = categories[index]

                            val totalPercentage = categories.sumOf {
                                it.partPercentage.text.toIntOrNull() ?: 0
                            }

                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                            ) {
                                CategoryCard(
                                    state = category,
                                    percentageExceeded = totalPercentage > 100,
                                    errorMessage = "Total percentage must be 100",
                                    onNameChanged = { name ->
                                        categories[index] = category.copy(
                                            name = name
                                        )
                                    },
                                    onPercentageChanged = { percentage ->
                                        categories[index] = category.copy(
                                            partPercentage = percentage
                                        )
                                    },
                                    onDescriptionChanged = { description ->
                                        categories[index] = category.copy(
                                            description = description
                                        )
                                    }
                                )

                                if (
                                    index == categories.size - 1 &&
                                    category.name.text.isNotEmpty()
                                ) categories.add(
                                    element = CategoryItemState()
                                )

                                if (
                                    index < categories.size - 1 &&
                                    category.name.text.isEmpty()
                                ) categories.removeAt(index = index)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                        .constrainAs(buttonContainer) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EvaluasiButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Save Categories",
                        buttonType = ButtonType.PRIMARY,
                        onClick = {
                            onAction(
                                AddCategoriesAction.AddCategories(
                                    categories = categories
                                        .filterIndexed { index, category ->
                                            index != categories.size - 1 ||
                                                    category.name.text.isNotEmpty()
                                        },
                                    classRoomId = classRoomId
                                )
                            )
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun AddCategoriesPreview() {
    val state by remember {
        mutableStateOf(
            AddCategoriesState()
        )
    }

    EvaluasiTheme {
        AddCategoriesScreen(
            state = state,
            onBackPressed = { },
            onClearStudentPressed = { },
            onAction = { },
            classRoomId = 0
        )
    }
}