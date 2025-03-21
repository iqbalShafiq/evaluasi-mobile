package id.usecase.assessment.presentation.screens.class_room.detail.assessment_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomState
import id.usecase.assessment.presentation.screens.class_room.detail.assessment_history.components.AssessmentSummaryItem
import id.usecase.assessment.presentation.screens.class_room.detail.assessment_history.components.MonthlySummaryItem
import id.usecase.core.presentation.ui.isCurrentMonth
import id.usecase.core.presentation.ui.isLastMonth
import id.usecase.core.presentation.ui.isOlderThanLastMonth
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.chip.FilterChipGroup

@Composable
private fun EmptyAssessmentState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(72.dp),
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Assessments Yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
            )
            Text(
                text = "Start by adding your first assessment",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun AssessmentHistoryTab(
    state: ClassRoomState,
    onDetailAssessmentEventClicked: (AssessmentEventUi) -> Unit,
    bottomPadding: Dp = 0.dp
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 16.dp
            )
    ) {
        if (state.assessmentEvents.isEmpty()) {
            EmptyAssessmentState()
            return@Column
        }

        // Monthly Assessment Summary
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Monthly Summary",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MonthlySummaryItem(
                        label = "This Month",
                        count = state.assessmentEvents.count {
                            it.eventDate.isCurrentMonth()
                        }
                    )
                    MonthlySummaryItem(
                        label = "Last Month",
                        count = state.assessmentEvents.count {
                            it.eventDate.isLastMonth()
                        }
                    )
                    MonthlySummaryItem(
                        label = "Total",
                        count = state.assessmentEvents.size
                    )
                }
            }
        }

        // Filter Chips
        var selectedFilter by remember { mutableStateOf("All") }
        val filters = listOf("All", "Recent", "Last Month", "Older")

        FilterChipGroup(
            selectedFilter = selectedFilter,
            filters = filters,
            onFilterSelected = { selectedFilter = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Assessment List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = bottomPadding)
        ) {
            items(
                state.assessmentEvents.filter {
                    when (selectedFilter) {
                        "Recent" -> it.eventDate.isCurrentMonth()
                        "Last Month" -> it.eventDate.isLastMonth()
                        "Older" -> it.eventDate.isOlderThanLastMonth()
                        else -> true
                    }
                }
            ) { assessment ->
                AssessmentSummaryItem(
                    assessment = assessment,
                    onDetailClicked = { onDetailAssessmentEventClicked(assessment) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AssessmentHistoryTabPreview() {
    EvaluasiTheme {
        AssessmentHistoryTab(
            state = ClassRoomState(
                assessmentEvents = listOf(
                    AssessmentEventUi(
                        id = 1,
                        categoryId = 1,
                        classId = 1,
                        name = "Assessment Name",
                        categoryName = "Category Name",
                        createdTime = "2022-09-01 08:00:00",
                        eventDate = "2022-09-01 08:00:00",
                        totalAssessment = 10,
                        lastModifiedTime = "2022-09-01 08:00:00",
                        isInProgress = false,
                        completionProgress = 0.5f
                    ), AssessmentEventUi(
                        id = 2,
                        categoryId = 1,
                        classId = 1,
                        name = "Assessment Name",
                        categoryName = "Category Name",
                        createdTime = "2022-08-01 08:00:00",
                        eventDate = "2022-08-01 08:00:00",
                        totalAssessment = 10,
                        lastModifiedTime = "2022-08-01 08:00:00",
                        isInProgress = false,
                        completionProgress = 0.5f
                    ), AssessmentEventUi(
                        id = 3,
                        categoryId = 1,
                        classId = 1,
                        name = "Assessment Name",
                        categoryName = "Category Name",
                        createdTime = "2022-07-01 08:00:00",
                        eventDate = "2022-07-01 08:00:00",
                        totalAssessment = 10,
                        lastModifiedTime = "2022-07-01 08:00:00",
                        isInProgress = false,
                        completionProgress = 0.5f
                    )
                )
            ),
            onDetailAssessmentEventClicked = {}
        )
    }
}