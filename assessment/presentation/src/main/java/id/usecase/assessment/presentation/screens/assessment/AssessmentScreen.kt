@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.assessment

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.StudentScoreUi
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentCard
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.dialog.EvaluasiDatePicker
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.dialog.StandardLoadingDialog
import id.usecase.designsystem.components.text_field.EvaluasiDropdown
import id.usecase.designsystem.components.text_field.EvaluasiMultipleChoiceDropdown
import id.usecase.designsystem.components.text_field.EvaluasiTextField
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AssessmentScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: AssessmentViewModel = koinViewModel(),
    classRoomId: Int,
    eventId: Int?,
    onBackPressed: () -> Unit,
    onAssessmentHasSaved: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val openAlertDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val showStartDatePicker = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(
            action = AssessmentAction.LoadAssessmentDetail(
                classRoomId = classRoomId,
                eventId = eventId
            )
        )
    }

    Log.d("TAG", "AssessmentScreenRoot: Event Id $eventId")

    when {
        showStartDatePicker.value -> {
            EvaluasiDatePicker(
                onDismiss = {
                    showStartDatePicker.value = false
                },
                onDateSelected = {
                    viewModel.onAction(
                        AssessmentAction.UpdateForms(
                            state.copy(
                                selectedDate = it ?: System.currentTimeMillis(),
                                startDateField = TextFieldValue(
                                    text = SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    ).format(it)
                                )
                            )
                        )
                    )
                    showStartDatePicker.value = false
                }
            )
        }
    }

    ObserveAsEvents(
        flow = viewModel.events,
        onEvent = { event ->
            when (event) {
                is AssessmentEvent.OnErrorOccurred -> {
                    openAlertDialog.value = true
                    errorMessage.value = event.errorMessage
                }

                is AssessmentEvent.AssessmentHasSaved -> {
                    onAssessmentHasSaved()
                }
            }
        }
    )

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

    AssessmentScreen(
        modifier = modifier,
        state = state,
        eventId = eventId,
        onBackPressed = onBackPressed,
        showStartDatePicker = {
            showStartDatePicker.value = true
        },
        onAction = viewModel::onAction
    )
}

@Composable
fun AssessmentScreen(
    modifier: Modifier = Modifier,
    eventId: Int?,
    onBackPressed: () -> Unit,
    state: AssessmentState,
    showStartDatePicker: () -> Unit,
    onAction: (AssessmentAction) -> Unit
) {
    val scrollState = rememberScrollState()
    var studentScoresY by remember { mutableFloatStateOf(0f) }
    var showStickyTitle by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState.value) {
        showStickyTitle = scrollState.value > studentScoresY
    }

    val assessments = remember(state.assessmentListField) {
        mutableStateListOf<StudentAssessmentState>().apply {
            addAll(state.assessmentListField)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                title = if (eventId == null) stringResource(R.string.create_assessment) else stringResource(
                    R.string.update_assessment
                ),
                navigationIcon = ImageVector.vectorResource(R.drawable.ic_rounded_arrow_back),
                onNavigationClicked = onBackPressed
            )
        },
        content = { innerPadding ->
            ConstraintLayout(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val (content, button) = createRefs()
                val (progress) = createRefs()
                LinearProgressIndicator(
                    progress = {
                        if (state.assessmentListField.isEmpty()) return@LinearProgressIndicator 0f
                        assessments.count {
                            (it.score.text.toDoubleOrNull() ?: 0.0) != 0.0
                        } / state.assessmentListField.size.toFloat()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .constrainAs(progress) {
                            top.linkTo(parent.top)
                        }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(content) {
                            top.linkTo(progress.bottom)
                            bottom.linkTo(button.top)
                            height = Dimension.fillToConstraints
                        }
                        .padding(top = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {

                            // Assessment Details Section
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                text = "Assessment Details",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Assessment Name Field
                            EvaluasiTextField(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Title",
                                placeholder = "Type name",
                                value = state.assessmentNameField,
                                onValueChange = {
                                    onAction(
                                        AssessmentAction.UpdateForms(
                                            state.copy(
                                                assessmentNameField = it
                                            )
                                        )
                                    )
                                }
                            )

                            // Purpose Name Field
                            EvaluasiTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                label = "Purpose",
                                placeholder = "Type descriptive purpose",
                                value = state.assessmentNameField,
                                onValueChange = {
                                    onAction(
                                        AssessmentAction.UpdateForms(
                                            state.copy(
                                                purposeField = it
                                            )
                                        )
                                    )
                                },
                                singleLine = false,
                                minLines = 1,
                                maxLines = 3
                            )

                            // Sections Field
                            EvaluasiMultipleChoiceDropdown(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                label = "Sections",
                                placeholder = "Choose sections",
                                items = state.sectionNameList,
                                selectedItems = state.selectedSectionNameList,
                                onItemsSelected = {
                                    onAction(
                                        AssessmentAction.UpdateForms(
                                            state.copy(
                                                selectedSectionNameList = it
                                            )
                                        )
                                    )
                                },
                                trailingContentForItem = { item ->
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = id.usecase.designsystem.R.drawable.ic_test_icon),
                                        contentDescription = "Info",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )

                            // Category Field
                            EvaluasiDropdown(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                label = "Category",
                                placeholder = "Choose category",
                                items = state.categoryNameList,
                                selectedItem = state.selectedCategoryName,
                                onItemSelected = { category ->
                                    onAction(
                                        AssessmentAction.OnCategorySelected(category)
                                    )
                                }
                            )

                            // Assessment Date Field
                            EvaluasiTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                label = "Assessment Date",
                                placeholder = "Pick date",
                                value = state.startDateField,
                                onValueChange = {
                                    onAction(
                                        AssessmentAction.UpdateForms(
                                            state.copy(
                                                startDateField = it
                                            )
                                        )
                                    )
                                },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = showStartDatePicker) {
                                        Icon(
                                            imageVector = Icons.Rounded.DateRange,
                                            contentDescription = "Calendar icon"
                                        )
                                    }
                                }
                            )
                        }

                        // Students Section
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .onGloballyPositioned { coordinates ->
                                    studentScoresY = coordinates.positionInParent().y
                                },
                            text = "Student Scores",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "${assessments.count { (it.score.text.toDoubleOrNull() ?: 0.0) != 0.0 }}/${state.assessmentListField.size} assessment of students",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 1500.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 12.dp)
                        ) {
                            items(assessments.size) { index ->
                                StudentAssessmentCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    state = assessments[index],
                                    onScoreChanged = { score ->
                                        assessments[index] = assessments[index].copy(
                                            score = score
                                        )
                                    },
                                    onCommentsChanged = { comment ->
                                        assessments[index] = assessments[index].copy(
                                            comments = comment
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // Sticky Header
                    AnimatedVisibility(
                        visible = showStickyTitle,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = state.assessmentNameField.text.ifEmpty { "Title has not added" },
                                style = MaterialTheme.typography.titleMedium,
                                color = if (state.assessmentNameField.text.isNotEmpty()) {
                                    MaterialTheme.colorScheme.primary
                                } else MaterialTheme.colorScheme.error
                            )

                            Text(
                                text = "${assessments.count { (it.score.text.toDoubleOrNull() ?: 0.0) != 0.0 }}/${state.assessmentListField.size} assessment of students",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Save Button
                EvaluasiButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(button) {
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(16.dp),
                    text = if (eventId == null) "Save Assessment" else "Update Assessment",
                    onClick = {
                        onAction(
                            AssessmentAction.SaveAssessmentEvent(
                                assessments = assessments
                            )
                        )
                    },
                    buttonType = ButtonType.PRIMARY,
                    enabled = state.isFormValid
                )
            }
        }
    )
}

@Preview
@Composable
private fun AssessmentScreenPreview() {
    EvaluasiTheme {
        AssessmentScreen(
            onAction = {},
            onBackPressed = {},
            showStartDatePicker = {},
            eventId = 1,
            state = AssessmentState(
                assessmentListField = listOf(
                    StudentAssessmentState(
                        data = StudentScoreUi(
                            assessmentId = 1,
                            studentId = 1,
                            studentName = "John Doe",
                            comments = "Good job",
                            score = 90.0,
                            avgScore = 80.0
                        )
                    ),
                    StudentAssessmentState(
                        data = StudentScoreUi(
                            assessmentId = 2,
                            studentId = 2,
                            studentName = "Jane Doe",
                            comments = "Good job",
                            score = 90.0,
                            avgScore = 80.0
                        )
                    ),
                    StudentAssessmentState(
                        data = StudentScoreUi(
                            assessmentId = 3,
                            studentId = 3,
                            studentName = "John Smith",
                            comments = "Good job",
                            score = 90.0,
                            avgScore = 80.0
                        )
                    )
                )
            )
        )
    }
}