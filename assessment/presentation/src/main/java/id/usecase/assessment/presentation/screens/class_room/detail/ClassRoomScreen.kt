@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.core.entry.FloatEntry
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.AnalyticsTab
import id.usecase.assessment.presentation.screens.class_room.detail.assessment_history.AssessmentHistoryTab
import id.usecase.assessment.presentation.screens.class_room.detail.class_overview.ClassOverviewTab
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.ActionItem
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.EvaluasiFloatingActionButton
import id.usecase.designsystem.components.dialog.StandardAlertDialog
import id.usecase.designsystem.components.dialog.StandardLoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun ClassRoomScreenRoot(
    modifier: Modifier = Modifier,
    classRoomId: Int,
    onBackPressed: () -> Unit,
    onDetailAssessmentEventClicked: (AssessmentEventUi) -> Unit,
    onSettingClicked: () -> Unit,
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
        onSettingClicked = onSettingClicked,
        onStudentEditClicked = onStudentEditClicked,
        onAddAssessmentClicked = onAddAssessmentClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassRoomScreen(
    modifier: Modifier = Modifier,
    state: ClassRoomState,
    onBackPressed: () -> Unit,
    onDetailAssessmentEventClicked: (AssessmentEventUi) -> Unit,
    onSettingClicked: () -> Unit,
    onStudentEditClicked: () -> Unit,
    onAddAssessmentClicked: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview", "Assessments", "Analytics")
    var fabHeight by remember { mutableIntStateOf(0) }
    val heightInDp = with(LocalDensity.current) { fabHeight.toDp() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EvaluasiTopAppBar(
                title = state.classRoom?.name ?: "Detail",
                navigationIcon = ImageVector.vectorResource(R.drawable.ic_rounded_arrow_back),
                onNavigationClicked = onBackPressed,
                trailingIcons = listOf(
                    ActionItem(
                        icon = ImageVector.vectorResource(R.drawable.ic_students),
                        onClick = {
                            onStudentEditClicked()
                        },
                        contentDescription = "Edit Student"
                    ),
                    ActionItem(
                        icon = Icons.Rounded.Settings,
                        onClick = {
                            onSettingClicked()
                        },
                        contentDescription = "Settings"
                    ),
                )
            )
        },
        floatingActionButton = {
            EvaluasiFloatingActionButton(
                modifier = Modifier
                    .onGloballyPositioned { fabHeight = it.size.height },
                text = "Add Assessment",
                icon = ImageVector.vectorResource(id = R.drawable.ic_edit),
                iconContentDescription = "Add button",
                onClickListener = onAddAssessmentClicked
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

@Composable
fun TabContent(
    modifier: Modifier = Modifier,
    paddingBottom: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        content()
        Spacer(modifier = Modifier.padding(bottom = paddingBottom + 24.dp))
    }
}

@Preview
@Composable
private fun ClassRoomPreview() {
    val state = ClassRoomState(
        assessmentEvents = listOf(
            AssessmentEventUi(
                id = 1,
                categoryId = 1,
                classId = 1,
                name = "Assessment Name",
                categoryName = "Monthly",
                createdTime = "2022-07-01 08:00:00",
                eventDate = "2022-07-01 08:00:00",
                totalAssessment = 10,
                lastModifiedTime = "2022-07-01 08:00:00",
                isInProgress = false,
                completionProgress = 0.5f
            ),
            AssessmentEventUi(
                id = 2,
                categoryId = 1,
                classId = 1,
                name = "Assessment Name",
                categoryName = "Mid",
                createdTime = "2022-07-01 08:00:00",
                eventDate = "2022-07-01 08:00:00",
                totalAssessment = 10,
                lastModifiedTime = "2022-07-01 08:00:00",
                isInProgress = false,
                completionProgress = 0.5f
            ),
            AssessmentEventUi(
                id = 3,
                categoryId = 1,
                classId = 1,
                name = "Assessment Name",
                categoryName = "Final",
                createdTime = "2022-07-01 08:00:00",
                eventDate = "2022-07-01 08:00:00",
                totalAssessment = 10,
                lastModifiedTime = "2022-07-01 08:00:00",
                isInProgress = false,
                completionProgress = 0.5f
            )
        ),
        classAverage = 80.0,
        performanceTrendData = listOf(
            FloatEntry(1f, 50f),
            FloatEntry(2f, 60f),
            FloatEntry(3f, 70f),
            FloatEntry(4f, 20f),
            FloatEntry(5f, 30f),
            FloatEntry(6f, 40f),
        ),
        categoryDistributionData = listOf(
            FloatEntry(0f, 20f),
            FloatEntry(1f, 30f),
            FloatEntry(2f, 50f),
        )
    )
    EvaluasiTheme {
        ClassRoomScreen(
            onBackPressed = { },
            onDetailAssessmentEventClicked = { },
            onSettingClicked = { },
            onStudentEditClicked = { },
            onAddAssessmentClicked = { },
            state = state
        )
    }
}