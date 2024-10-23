@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentCardState
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.dialog.StandardDatePicker
import id.usecase.designsystem.components.text_field.EvaluasiTextField
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CreateClassRoomScreenRoot(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onClassHasCreated: (classRoom: ClassRoomUi) -> Unit,
    viewModel: CreateClassRoomViewModel = koinViewModel()
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val showStartDatePicker = remember { mutableStateOf(false) }

    when {
        showStartDatePicker.value -> {
            StandardDatePicker(
                onDismiss = {
                    showStartDatePicker.value = false
                },
                onDateSelected = {
                    viewModel.onAction(
                        CreateClassRoomAction.SetStartDate(
                            startDate = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(it)
                        )
                    )
                    showStartDatePicker.value = false
                }
            )
        }
    }

    ObserveAsEvents(
        flow = viewModel.events
    ) { event ->
        when (event) {
            is CreateClassRoomEvent.OnErrorOccurred -> {
                openAlertDialog.value = true
                errorMessage.value = event.message
            }

            is CreateClassRoomEvent.OnClassRoomCreated -> {
                onClassHasCreated(event.classRoomUi)
            }
        }
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

    CreateClassRoomScreen(
        modifier = modifier,
        onBackPressed = onBackPressed,
        showStartDatePicker = {
            showStartDatePicker.value = true
        },
        state = viewModel.state.value
    )
}

@Composable
fun CreateClassRoomScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    showStartDatePicker: () -> Unit,
    state: CreateClassRoomState
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = "Create Class Room",
                navigationIcon = ImageVector.vectorResource(
                    R.drawable.rounded_arrow_back
                ),
                onNavigationClicked = onBackPressed
            )
        },
        content = { innerPadding ->
            ConstraintLayout(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
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
                    EvaluasiTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Class Name",
                        placeholder = "Type name",
                        state = state.classRoomName,
                        inputType = KeyboardType.Text
                    )

                    EvaluasiTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Subject",
                        placeholder = "Type subject",
                        state = state.subject,
                        inputType = KeyboardType.Text
                    )

                    EvaluasiTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Start Class Date",
                        placeholder = "Pick date",
                        state = state.startDate,
                        clickAction = true,
                        inputType = KeyboardType.Text,
                        onClick = {
                            showStartDatePicker()
                        }
                    )
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EvaluasiButton(
                        modifier = Modifier.weight(1f),
                        text = "Create",
                        buttonType = ButtonType.PRIMARY,
                        onClick = { }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun CreateClassRoomPreview() {
    EvaluasiTheme {
        val showStartDatePicker = remember { mutableStateOf(false) }

        when {
            showStartDatePicker.value -> {
                StandardDatePicker(
                    onDismiss = {
                        showStartDatePicker.value = false
                    },
                    onDateSelected = {
                        showStartDatePicker.value = false
                    }
                )
            }
        }

        CreateClassRoomScreen(
            onBackPressed = { },
            state = CreateClassRoomState(
                classRoomName = TextFieldState(),
                subject = TextFieldState(),
                startDate = TextFieldState(),
                students = listOf(
                    AddStudentCardState(
                        identifier = TextFieldState(),
                        name = TextFieldState()
                    )
                )
            ),
            showStartDatePicker = {
                showStartDatePicker.value = true
            }
        )
    }
}