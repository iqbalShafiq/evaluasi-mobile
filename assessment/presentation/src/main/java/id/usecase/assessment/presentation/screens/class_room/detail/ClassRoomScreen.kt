@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.AlertUi
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.ActionItem
import id.usecase.designsystem.components.app_bar.EvaluasiBottomAppBar
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar

@Composable
fun ClassRoomScreenRoot(modifier: Modifier = Modifier) {

}

@Composable
fun ClassRoomScreen(
    modifier: Modifier = Modifier,
    state: ClassRoomState,
    onBioEditClicked: () -> Unit,
    onCategoryEditClicked: () -> Unit,
    onAddAssessmentClicked: () -> Unit,
    onStudentEditClicked: () -> Unit
) {
    var fabHeight by remember {
        mutableIntStateOf(0)
    }

    val heightInDp = with(LocalDensity.current) { fabHeight.toDp() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EvaluasiTopAppBar(
                title = "Class Room Detail",
                navigationIcon = ImageVector.vectorResource(
                    R.drawable.rounded_arrow_back
                ),
                trailingIcon = ImageVector.vectorResource(
                    R.drawable.ic_students
                ),
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
                            onStudentEditClicked()
                        },
                        contentDescription = "Class Room Alerts"
                    ),
                )
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Assessment History",
                    style = MaterialTheme.typography.titleMedium,
                )

                if (state.assessmentEvents.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = "Assessment has not been taken yet",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.error
                        ),
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp),
                    contentPadding = PaddingValues(bottom = heightInDp)
                ) {
                    items(state.assessmentEvents) { events ->
                        AssessmentHistoryCard(
                            modifier = Modifier,
                            eventUi = events,
                            onDetailClicked = { },
                            onAlertClicked = { }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun ClassRoomPreview() {
    val state = ClassRoomState(
        alerts = listOf(
            AlertUi(
                studentName = "John Doe",
                studentIdentifier = 1,
                studentAverageScore = 60.7,
                classAverageScore = 70.5,
                detectedDate = "2021-12-12",
                alert = "Student is not active in class"
            ),
            AlertUi(
                studentName = "Jane Doe",
                studentIdentifier = 2,
                studentAverageScore = 80.7,
                classAverageScore = 70.5,
                detectedDate = "2021-12-12",
                alert = "Student is not active in class"
            ),
        ),
        assessmentEvents = listOf(
            AssessmentEventUi(
                id = 1,
                name = "Assessment 1",
                eventDate = "03 Oct 2024 12:00:00",
                createdTime = "03 Oct 2024 12:00:00",
                assessedStudentCount = 10,
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
                assessedStudentCount = 10,
                categoryId = 1,
                categoryName = "Category 1",
                classId = 1,
                lastModifiedTime = "03 Oct 2024 12:00:00",
            )
        )
    )
    EvaluasiTheme {
        ClassRoomScreen(
            onBioEditClicked = { },
            onCategoryEditClicked = { },
            onStudentEditClicked = { },
            onAddAssessmentClicked = { },
            state = state
        )
    }
}