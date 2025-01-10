@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.AnalyticsTab
import id.usecase.assessment.presentation.screens.class_room.detail.assessment_history.AssessmentHistoryTab
import id.usecase.assessment.presentation.screens.class_room.detail.class_overview.ClassOverviewTab
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.ActionItem
import id.usecase.designsystem.components.app_bar.EvaluasiBottomAppBar
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.dialog.StandardLoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun ClassRoomScreenRoot(
    modifier: Modifier = Modifier,
    classRoomId: Int,
    onBackPressed: () -> Unit,
    onDetailAssessmentEventClicked: (AssessmentEventUi) -> Unit,
    onBioEditClicked: () -> Unit,
    onCategoryEditClicked: () -> Unit,
    onAddAssessmentClicked: () -> Unit,
    onStudentEditClicked: () -> Unit,
    onAlertClicked: () -> Unit,
    viewModel: ClassRoomViewModel = koinViewModel()
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(ClassRoomAction.LoadClassRoom(classRoomId))
    }

    ObserveAsEvents(
        flow = viewModel.events,
        onEvent = { event ->
            when (event) {
                is ClassRoomEvent.OnErrorOccurred -> {
                    openAlertDialog.value = true
                    errorMessage.value = event.message
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

    if (viewModel.state.value.isLoading) {
        StandardLoadingDialog()
    }

    ClassRoomScreen(
        modifier = modifier,
        state = viewModel.state.value,
        onBackPressed = onBackPressed,
        onDetailAssessmentEventClicked = onDetailAssessmentEventClicked,
        onBioEditClicked = onBioEditClicked,
        onCategoryEditClicked = onCategoryEditClicked,
        onAddAssessmentClicked = onAddAssessmentClicked,
        onStudentEditClicked = onStudentEditClicked,
        onAlertClicked = onAlertClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassRoomScreen(
    modifier: Modifier = Modifier,
    state: ClassRoomState,
    onBackPressed: () -> Unit,
    onDetailAssessmentEventClicked: (AssessmentEventUi) -> Unit,
    onBioEditClicked: () -> Unit,
    onCategoryEditClicked: () -> Unit,
    onAddAssessmentClicked: () -> Unit,
    onStudentEditClicked: () -> Unit,
    onAlertClicked: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview", "Assessment History", "Analytics")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EvaluasiTopAppBar(
                title = "Class Room Detail",
                navigationIcon = ImageVector.vectorResource(R.drawable.rounded_arrow_back),
                onNavigationClicked = onBackPressed,
                trailingIcon = ImageVector.vectorResource(R.drawable.ic_students),
                onTrailingIconClicked = { onStudentEditClicked() }
            )
        },
        bottomBar = {
            EvaluasiBottomAppBar(
                modifier = Modifier,
                navigationIcon = ImageVector.vectorResource(
                    R.drawable.ic_add
                ),
                onNavigationClicked = {
                    onAddAssessmentClicked()
                },
                actionItemList = listOf(
                    ActionItem(
                        icon = ImageVector.vectorResource(
                            R.drawable.ic_edit
                        ),
                        onClick = {
                            onBioEditClicked()
                        },
                        contentDescription = "Edit Class Room Bio"
                    ),
                    ActionItem(
                        icon = ImageVector.vectorResource(
                            R.drawable.ic_category
                        ),
                        onClick = {
                            onCategoryEditClicked()
                        },
                        contentDescription = "Edit Categories"
                    ),
                    ActionItem(
                        icon = ImageVector.vectorResource(
                            R.drawable.ic_information
                        ),
                        onClick = {
                            onAlertClicked()
                        },
                        contentDescription = "Class Room Alerts"
                    ),
                )
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Tab Row
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }

                // Content based on selected tab
                when (selectedTab) {
                    0 -> ClassOverviewTab(state)
                    1 -> AssessmentHistoryTab(state, onDetailAssessmentEventClicked)
                    2 -> AnalyticsTab(state)
                }
            }
        }
    )
}

@Preview
@Composable
private fun ClassRoomPreview() {
    val state = ClassRoomState(
        assessmentEvents = listOf(
            AssessmentEventUi(
                id = 1,
                name = "Assessment 1",
                eventDate = "03 Oct 2024 12:00:00",
                createdTime = "03 Oct 2024 12:00:00",
                totalAssessment = 10,
                categoryId = 1,
                categoryName = "Category 1",
                classId = 1,
                lastModifiedTime = "03 Oct 2024 12:00:00",
            ),
            AssessmentEventUi(
                id = 2,
                name = "Assessment 2",
                eventDate = "03 Oct 2024 12:00:00",
                createdTime = "03 Oct 2024 12:00:00",
                totalAssessment = 10,
                categoryId = 1,
                categoryName = "Category 1",
                classId = 1,
                lastModifiedTime = "03 Oct 2024 12:00:00",
            )
        )
    )
    EvaluasiTheme {
        ClassRoomScreen(
            onBackPressed = { },
            onDetailAssessmentEventClicked = { },
            onBioEditClicked = { },
            onCategoryEditClicked = { },
            onStudentEditClicked = { },
            onAddAssessmentClicked = { },
            onAlertClicked = { },
            state = state
        )
    }
}