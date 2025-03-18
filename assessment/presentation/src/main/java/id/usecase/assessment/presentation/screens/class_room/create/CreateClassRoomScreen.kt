@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.class_room.create.students.components.AddStudentItemState
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.dialog.EvaluasiDatePicker
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.text_field.EvaluasiTextField
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.Locale

@Composable
fun CreateClassRoomScreenRoot(
    modifier: Modifier = Modifier,
    classRoomId: String? = null,
    onBackPressed: () -> Unit,
    onClassHasCreated: (classRoom: ClassRoomUi) -> Unit,
    onClassRoomHasUpdated: () -> Unit,
    viewModel: CreateClassRoomViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var openAlertDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showStartDatePicker by remember { mutableStateOf(false) }

    // Load class room detail
    LaunchedEffect(key1 = Unit) {
        if (classRoomId != null) {
            viewModel.onAction(
                action = CreateClassRoomAction.LoadClassRoomDetail(
                    classRoomId = classRoomId
                )
            )
        }
    }

    when {
        showStartDatePicker -> {
            EvaluasiDatePicker(
                onDismiss = {
                    showStartDatePicker = false
                },
                onDateSelected = {
                    viewModel.onAction(
                        CreateClassRoomAction.UpdateTextField(
                            state = state.copy(
                                startDate = TextFieldValue(
                                    SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    ).format(it)
                                )
                            )
                        )
                    )
                    showStartDatePicker = false
                }
            )
        }
    }

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is CreateClassRoomEvent.OnErrorOccurred -> {
                openAlertDialog = true
                errorMessage = event.message
            }

            is CreateClassRoomEvent.OnClassRoomCreated -> {
                onClassHasCreated(event.classRoomUi)
                Log.d("TAG", "CreateClassRoomScreenRoot: ${event.classRoomUi}")
            }

            is CreateClassRoomEvent.OnClassRoomHasUpdated -> {
                onClassRoomHasUpdated()
            }
        }
    }

    if (openAlertDialog) {
        StandardAlertDialog(
            onDismissRequest = {
                openAlertDialog = false
            },
            onConfirmation = {
                openAlertDialog = false
            },
            dialogTitle = "Error",
            dialogText = errorMessage
        )
    }

    CreateClassRoomScreen(
        modifier = modifier,
        onBackPressed = onBackPressed,
        onCreateButtonClicked = {
            viewModel.onAction(CreateClassRoomAction.CreateClassRoom)
        },
        showStartDatePicker = {
            showStartDatePicker = true
        },
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateClassRoomScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onCreateButtonClicked: () -> Unit,
    showStartDatePicker: () -> Unit,
    onAction: (CreateClassRoomAction) -> Unit,
    state: CreateClassRoomState
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = if (state.isEditing) "Edit Class Room" else "Create Class Room",
                navigationIcon = ImageVector.vectorResource(R.drawable.ic_rounded_arrow_back),
                navigationIconTint = MaterialTheme.colorScheme.onSurface,
                onNavigationClicked = onBackPressed
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                // Form Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    // Basic Information Card
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Basic Information",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            EvaluasiTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.classRoomName,
                                onValueChange = {
                                    onAction(
                                        CreateClassRoomAction.UpdateTextField(
                                            state = state.copy(
                                                classRoomName = it
                                            )
                                        )
                                    )
                                },
                                label = "Class Name",
                                placeholder = "e.g., Class 10A Mathematics",
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_school),
                                        contentDescription = null
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            EvaluasiTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.subject,
                                onValueChange = {
                                    onAction(
                                        CreateClassRoomAction.UpdateTextField(
                                            state = state.copy(
                                                subject = it
                                            )
                                        )
                                    )
                                },
                                label = "Subject",
                                placeholder = "e.g., Mathematics",
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Email,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }

                    // Schedule Card
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Timeline",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            EvaluasiTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.startDate,
                                onValueChange = {
                                    onAction(
                                        CreateClassRoomAction.UpdateTextField(
                                            state = state.copy(
                                                startDate = it
                                            )
                                        )
                                    )
                                },
                                label = "Start Date",
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = showStartDatePicker) {
                                        Icon(
                                            imageVector = Icons.Rounded.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            EvaluasiTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.longPeriod,
                                onValueChange = {
                                    onAction(
                                        CreateClassRoomAction.UpdateTextField(
                                            state = state.copy(
                                                longPeriod = it
                                            )
                                        )
                                    )
                                },
                                label = "Long Period (in Month)",
                                keyboardType = KeyboardType.Number,
                            )
                        }
                    }

                    // Additional Settings Card
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Additional Settings",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Class Description
                            EvaluasiTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.description,
                                onValueChange = {
                                    onAction(
                                        CreateClassRoomAction.UpdateTextField(
                                            state = state.copy(
                                                description = it
                                            )
                                        )
                                    )
                                },
                                label = "Class Description",
                                placeholder = "Add a description for your class",
                                minLines = 3,
                                maxLines = 5
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Class Schedule
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Class Schedule",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Switch(
                                    checked = state.hasSchedule,
                                    onCheckedChange = {
                                        onAction(
                                            CreateClassRoomAction.UpdateTextField(
                                                state = state.copy(
                                                    hasSchedule = it
                                                )
                                            )
                                        )
                                    }
                                )
                            }

                            AnimatedVisibility(visible = state.hasSchedule) {
                                Column {
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Meeting Days
                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        DayOfWeek.entries.forEach { day ->
                                            FilterChip(
                                                selected = day in state.selectedDays,
                                                onClick = {
                                                    onAction(
                                                        CreateClassRoomAction.UpdateTextField(
                                                            state = state.copy(
                                                                selectedDays = if (day in state.selectedDays) {
                                                                    state.selectedDays - day
                                                                } else {
                                                                    state.selectedDays + day
                                                                }
                                                            )
                                                        )
                                                    )
                                                },
                                                label = { Text(day.name.substring(0, 3)) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Create Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = onCreateButtonClicked,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.isFormValid
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = if (state.isEditing) "Save Changes" else "Create Class",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun CreateClassRoomPreview() {
    EvaluasiTheme {
        var showStartDatePicker by remember { mutableStateOf(false) }
        var state by remember {
            mutableStateOf(
                CreateClassRoomState(
                    classRoomName = TextFieldValue(),
                    subject = TextFieldValue(),
                    startDate = TextFieldValue(),
                    students = listOf(
                        AddStudentItemState(
                            identifier = TextFieldValue(),
                            name = TextFieldValue()
                        )
                    )
                )
            )
        }

        when {
            showStartDatePicker -> {
                EvaluasiDatePicker(
                    onDismiss = {
                        showStartDatePicker = false
                    },
                    onDateSelected = {
                        showStartDatePicker = false
                    }
                )
            }
        }

        CreateClassRoomScreen(
            onBackPressed = { },
            onCreateButtonClicked = { },
            state = state,
            showStartDatePicker = {
                showStartDatePicker = true
            },
            onAction = { action ->
                when (action) {
                    CreateClassRoomAction.CreateClassRoom -> TODO()
                    is CreateClassRoomAction.LoadClassRoomDetail -> TODO()
                    is CreateClassRoomAction.UpdateTextField -> {
                        state = action.state
                    }
                }
            }
        )
    }
}