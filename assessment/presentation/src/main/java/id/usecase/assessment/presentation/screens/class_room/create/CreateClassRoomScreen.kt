@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentItemState
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.dialog.StandardDatePicker
import id.usecase.designsystem.components.dialog.StandardLoadingDialog
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.Locale

@Composable
fun CreateClassRoomScreenRoot(
    modifier: Modifier = Modifier,
    classRoomId: Int? = null,
    onBackPressed: () -> Unit,
    onClassHasCreated: (classRoom: ClassRoomUi) -> Unit,
    viewModel: CreateClassRoomViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val openAlertDialog = remember { mutableStateOf(false) }
    val openLoadingDialog = remember { mutableStateOf(false) }
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
                            text = TextFieldValue(
                                text = SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                ).format(it)
                            )
                        )
                    )
                    showStartDatePicker.value = false
                }
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (classRoomId != null) {
            viewModel.onAction(
                action = CreateClassRoomAction.LoadClassRoomDetail(
                    classRoomId = classRoomId
                )
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
                openLoadingDialog.value = false
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

    if (openLoadingDialog.value) {
        StandardLoadingDialog()
    }

    CreateClassRoomScreen(
        modifier = modifier,
        onBackPressed = onBackPressed,
        onCreatePressed = {
            viewModel.onAction(CreateClassRoomAction.CreateClassRoom)
            openLoadingDialog.value = true
        },
        showStartDatePicker = {
            showStartDatePicker.value = true
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
    onCreatePressed: () -> Unit,
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
                navigationIcon = ImageVector.vectorResource(R.drawable.rounded_arrow_back),
                navigationIconTint = MaterialTheme.colorScheme.onSurface,
                onNavigationClicked = onBackPressed
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
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

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.classRoomName,
                                shape = MaterialTheme.shapes.small,
                                onValueChange = { onAction(CreateClassRoomAction.SetClassRoomName(it)) },
                                label = { Text(text = "Class Name") },
                                placeholder = { Text("e.g., Class 10A Mathematics") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.AccountCircle,
                                        contentDescription = null
                                    )
                                },
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.subject,
                                shape = MaterialTheme.shapes.small,
                                onValueChange = { onAction(CreateClassRoomAction.SetSubject(it)) },
                                label = { Text("Subject") },
                                placeholder = { Text("e.g., Mathematics") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.MailOutline,
                                        contentDescription = null
                                    )
                                },
                                singleLine = true
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

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.startDate,
                                shape = MaterialTheme.shapes.small,
                                onValueChange = {
                                    onAction(CreateClassRoomAction.SetStartDate(it))
                                },
                                label = { Text("Start Date") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = showStartDatePicker) {
                                        Icon(
                                            imageVector = Icons.Filled.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                },
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.endDate,
                                shape = MaterialTheme.shapes.small,
                                onValueChange = {
                                    onAction(CreateClassRoomAction.SetEndDate(it))
                                },
                                label = { Text("End Date") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = showStartDatePicker) {
                                        Icon(
                                            imageVector = Icons.Filled.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                },
                                singleLine = true
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
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.description,
                                shape = MaterialTheme.shapes.small,
                                onValueChange = { onAction(CreateClassRoomAction.SetDescription(it)) },
                                label = { Text("Class Description") },
                                placeholder = { Text("Add a description for your class") },
                                minLines = 3,
                                maxLines = 5
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Meeting Schedule
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Meeting Schedule",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Switch(
                                    checked = state.hasMeetingSchedule,
                                    onCheckedChange = { /* update value */ }
                                )
                            }

                            AnimatedVisibility(visible = state.hasMeetingSchedule) {
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
                                                onClick = { /* update selected days */ },
                                                label = { Text(day.name.substring(0, 3)) }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Meeting Time
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            modifier = Modifier.weight(1f),
                                            value = state.startTime,
                                            onValueChange = { },
                                            label = { Text("Start Time") },
                                            readOnly = true,
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Outlined.DateRange,
                                                    contentDescription = null
                                                )
                                            }
                                        )

                                        OutlinedTextField(
                                            modifier = Modifier.weight(1f),
                                            value = state.endTime,
                                            onValueChange = { },
                                            label = { Text("End Time") },
                                            readOnly = true,
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Outlined.DateRange,
                                                    contentDescription = null
                                                )
                                            }
                                        )
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
                        onClick = onCreatePressed,
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
            onCreatePressed = { },
            state = CreateClassRoomState(
                classRoomName = TextFieldValue(),
                subject = TextFieldValue(),
                startDate = TextFieldValue(),
                students = listOf(
                    AddStudentItemState(
                        identifier = TextFieldState(),
                        name = TextFieldState()
                    )
                )
            ),
            showStartDatePicker = {
                showStartDatePicker.value = true
            },
            onAction = { }
        )
    }
}