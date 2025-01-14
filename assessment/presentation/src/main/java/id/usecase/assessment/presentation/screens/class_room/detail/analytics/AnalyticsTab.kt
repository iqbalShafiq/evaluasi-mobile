package id.usecase.assessment.presentation.screens.class_room.detail.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.model.CategoryAnalysis
import id.usecase.assessment.presentation.model.StudentProgress
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomState
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.components.CategoryAnalysisChart
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.components.PerformanceDistributionChart
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.components.StudentProgressList
import id.usecase.designsystem.EvaluasiTheme

@Composable
fun AnalyticsTab(
    state: ClassRoomState,
    bottomPadding: Dp = 0.dp
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = bottomPadding
        )
    ) {
        item {

            // Performance Distribution Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Performance Distribution",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    PerformanceDistributionChart(state.performanceDistribution)
                }
            }

            // Student Progress Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Student Progress",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    StudentProgressList(state.studentProgress)
                }
            }

            // Category Analysis Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Category Analysis",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CategoryAnalysisChart(state.categoryAnalysis)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnalyticsTabPreview() {
    EvaluasiTheme {
        AnalyticsTab(
            state = ClassRoomState(
                performanceDistribution = mapOf(
                    "Poor" to 0.2f,
                    "Average" to 0.3f,
                    "Good" to 0.5f
                ),
                studentProgress = listOf(
                    StudentProgress("Student 1", 0.5f, "2021-01-01"),
                    StudentProgress("Student 2", 0.7f, "2021-01-01"),
                    StudentProgress("Student 3", 0.9f, "2021-01-01")
                ),
                categoryAnalysis = listOf(
                    CategoryAnalysis(
                        "Category 1",
                        0.5f,
                        5,
                    )
                )
            )
        )
    }
}