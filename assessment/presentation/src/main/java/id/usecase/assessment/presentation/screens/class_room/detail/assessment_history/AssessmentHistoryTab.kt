package id.usecase.assessment.presentation.screens.class_room.detail.assessment_history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomState
import id.usecase.core.presentation.ui.isCurrentMonth
import id.usecase.core.presentation.ui.isLastMonth
import id.usecase.core.presentation.ui.isOlderThanLastMonth
import id.usecase.designsystem.components.chip.FilterChipGroup

@Composable
fun AssessmentHistoryTab(
    state: ClassRoomState,
    onDetailAssessmentEventClicked: (AssessmentEventUi) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Monthly Assessment Summary
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Monthly Summary",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//                    AssessmentSummaryItem(
//                        label = "This Month",
//                        count = state.assessmentEvents.count {
//                            it.eventDate.isCurrentMonth()
//                        }
//                    )
//                    AssessmentSummaryItem(
//                        label = "Last Month",
//                        count = state.assessmentEvents.count {
//                            it.eventDate.isLastMonth()
//                        }
//                    )
//                    AssessmentSummaryItem(
//                        label = "Total",
//                        count = state.assessmentEvents.size
//                    )
                }
            }
        }

        // Assessment List with Filtering
        var selectedFilter by remember { mutableStateOf("All") }
        val filters = listOf("All", "Recent", "Last Month", "Older")

        FilterChipGroup(
            selectedFilter = selectedFilter,
            filters = filters,
            onFilterSelected = { selectedFilter = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                AssessmentHistoryCard(
                    modifier = Modifier.fillMaxWidth(),
                    eventUi = assessment,
                    onDetailClicked = { onDetailAssessmentEventClicked(assessment) },
                    onAlertClicked = { }
                )
            }
        }
    }
}