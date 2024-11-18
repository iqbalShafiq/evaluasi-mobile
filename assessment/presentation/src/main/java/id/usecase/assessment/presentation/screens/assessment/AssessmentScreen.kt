@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.assessment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.StudentScoreUi
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentCard
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.dialog.StandardDatePicker
import id.usecase.designsystem.components.dialog.StandardLoadingDialog
import id.usecase.designsystem.components.text_field.EvaluasiTextField
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AssessmentScreenRoot(
    modifier: Modifier = Modifier,
    classRoomId: Int,
    eventId: Int?,
    viewModel: AssessmentViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    onAssessmentHasSaved: () -> Unit
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val showStartDatePicker = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        if (eventId != null) {
            viewModel.onAction(
                action = AssessmentAction.LoadAssessmentDetail(
                    classRoomId = classRoomId,
                    eventId = eventId
                )
            )
        }
    }

    when {
        showStartDatePicker.value -> {
            StandardDatePicker(
                onDismiss = {
                    showStartDatePicker.value = false
                },
                onDateSelected = {
                    viewModel.onAction(
                        AssessmentAction.UpdateEventDate(
                            date = SimpleDateFormat(
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

    if (viewModel.state.value.isLoading) StandardLoadingDialog()

    AssessmentScreen(
        modifier = modifier,
        state = viewModel.state.value,
        onBackPressed = onBackPressed,
        onAction = viewModel::onAction
    )
}

@Composable
fun AssessmentScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    state: AssessmentState,
    onAction: (AssessmentAction) -> Unit
) {
    val assessments = remember { mutableStateListOf<StudentAssessmentState>() }
    assessments.clear()
    assessments.addAll(state.assessmentListField)

    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                title = stringResource(R.string.create_assessment),
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
                val (content, button) = createRefs()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(content) {
                            top.linkTo(parent.top)
                            bottom.linkTo(button.top)
                            height = Dimension.fillToConstraints
                        }
                ) {
                    EvaluasiTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Assessment Name",
                        placeholder = "Type name",
                        state = state.assessmentNameField,
                        inputType = KeyboardType.Text
                    )

                    EvaluasiTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Assessment Date",
                        placeholder = "Pick date",
                        state = state.assessmentNameField,
                        inputType = KeyboardType.Text
                    )

                    EvaluasiTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Category",
                        placeholder = "Choose category",
                        state = state.assessmentNameField,
                        inputType = KeyboardType.Text
                    )

                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "Students",
                        style = MaterialTheme.typography.labelMedium
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 8.dp)
                    ) {
                        items(assessments.size) { index ->
                            val assessment = assessments[index]
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                            ) {
                                StudentAssessmentCard(
                                    state = assessment
                                )

                                if (
                                    index == assessments.size - 1 &&
                                    assessment.score.text.isNotEmpty()
                                ) assessments.add(StudentAssessmentState())

                                if (
                                    index != assessments.size - 1 &&
                                    assessment.score.text.isEmpty()
                                ) assessments.removeAt(index)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                EvaluasiButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(button) {
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(vertical = 12.dp),
                    text = "Save Assessment",
                    onClick = {
                        onAction(
                            AssessmentAction.SaveAssessmentEvent(
                                assessments = assessments
                                    .filterIndexed { index, assessment ->
                                        index != assessments.size - 1 ||
                                                assessment.score.text.isNotEmpty()
                                    }
                            )
                        )
                    },
                    buttonType = ButtonType.INVERSE
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