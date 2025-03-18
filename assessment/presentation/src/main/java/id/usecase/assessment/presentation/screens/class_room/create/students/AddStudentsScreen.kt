@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create.students

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.students.components.AddStudentCard
import id.usecase.assessment.presentation.screens.class_room.create.students.components.AddStudentItemState
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.dialog.StandardLoadingDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddStudentsScreenRoot(
    modifier: Modifier = Modifier,
    classRoomId: String,
    onBackPressed: () -> Unit,
    onStudentHasAdded: () -> Unit,
    openAutoFillScanner: () -> Unit,
    viewModel: AddStudentsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val openAlertDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    ObserveAsEvents(
        flow = viewModel.events,
        onEvent = { event ->
            when (event) {
                is AddStudentsEvent.OnErrorOccurred -> {
                    errorMessage.value = event.message
                    openAlertDialog.value = true
                }

                AddStudentsEvent.OnStudentsHasAdded -> {
                    onStudentHasAdded()
                }
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(
            action = AddStudentsAction.LoadStudents(
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

    if (state.isLoading) StandardLoadingDialog()

    AddStudentsScreen(
        modifier = modifier,
        classRoomId = classRoomId,
        onBackPressed = onBackPressed,
        onClearStudentPressed = {},
        onPastePressed = openAutoFillScanner,
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
        },
    )
}

@Composable
fun AddStudentsScreen(
    modifier: Modifier = Modifier,
    classRoomId: String,
    onBackPressed: () -> Unit,
    onClearStudentPressed: () -> Unit,
    onPastePressed: () -> Unit,
    onAction: (AddStudentsAction) -> Unit,
    state: AddStudentsState
) {
    val students = remember { mutableStateListOf<AddStudentItemState>() }

    Scaffold(
        topBar = {
            EvaluasiTopAppBar(
                title = "Add Students",
                navigationIcon = ImageVector.vectorResource(R.drawable.ic_rounded_arrow_back),
                navigationIconTint = MaterialTheme.colorScheme.onSurface,
                onNavigationClicked = onBackPressed,
                trailingIcons = listOf(
//                    ActionItem(
//                        icon = Icons.Rounded.Clear,
//                        contentDescription = "Cross X",
//                        onClick = onClearStudentPressed
//                    ),
//                    ActionItem(
//                        icon = ImageVector.vectorResource(R.drawable.ic_clipboard),
//                        contentDescription = "Clipboard",
//                        onClick = onPastePressed
//                    ),
                )
            )
        },
        content = { innerPadding ->
            val scope = rememberCoroutineScope()
            var progressVisible by remember { mutableStateOf(false) }

            students.clear()
            students.addAll(state.studentList)

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
                    LinearProgressIndicator(
                        progress = { students.count { it.isValid }.toFloat() / students.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .alpha(if (progressVisible) 1f else 0f)
                    )

                    Text(
                        text = "Students",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn {
                        items(students.size) { index ->
                            val student = students[index]

                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                            ) {
                                AddStudentCard(
                                    state = student,
                                    onIdentifierChanged = { identifier ->
                                        students[index] = student.copy(
                                            identifier = identifier,
                                            isValid = student.name.text.isNotEmpty()
                                        )
                                    },
                                    onNameChanged = { name ->
                                        students[index] = student.copy(
                                            name = name,
                                            isValid = student.identifier.text.isNotEmpty() ||
                                                    student.name.text.isNotEmpty()
                                        )
                                    }
                                )

                                if (
                                    index == students.size - 1 &&
                                    student.name.text.isNotEmpty()
                                ) students.add(AddStudentItemState())

                                if (
                                    index != students.size - 1 &&
                                    student.name.text.isEmpty()
                                ) students.removeAt(index)
                            }

                            Spacer(modifier = Modifier.padding(12.dp))
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
                        text = "Save Students",
                        buttonType = ButtonType.PRIMARY,
                        enabled = students
                            .takeIf { it.size > 1 }
                            ?.dropLast(1)
                            ?.all { it.isValid } == true,
                        onClick = {
                            scope.launch {
                                progressVisible = true
                                onAction(
                                    AddStudentsAction.AddStudents(
                                        students = students
                                            .filterIndexed { index, student ->
                                                index != students.size - 1 ||
                                                        student.name.text.isNotEmpty()
                                            },
                                        classRoomId = classRoomId
                                    )
                                )
                                progressVisible = false
                            }
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun AddStudentsPreview() {
    val state = remember { AddStudentsState() }
    EvaluasiTheme {
        AddStudentsScreen(
            state = state,
            classRoomId = "0",
            onBackPressed = { },
            onClearStudentPressed = { },
            onPastePressed = { },
            onAction = { }
        )
    }
}