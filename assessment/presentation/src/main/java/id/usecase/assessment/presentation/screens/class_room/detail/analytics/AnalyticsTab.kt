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
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomState
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.components.CategoryAnalysisChart
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.components.PerformanceDistributionChart
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.components.SectionScoreDistributionChart
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.components.SectionUsageDistributionChart
import id.usecase.assessment.presentation.screens.class_room.detail.analytics.components.StudentProgressList
import id.usecase.assessment.presentation.screens.class_room.detail.components.EmptyDataText
import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import id.usecase.core.domain.assessment.model.analytics.StudentProgress
import id.usecase.designsystem.EvaluasiTheme

@Composable
fun AnalyticsTab(
    state: ClassRoomState,
    bottomPadding: Dp = 0.dp
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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

                    if (state.performanceDistribution.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    }

                    PerformanceDistributionChart(state.performanceDistribution.toSortedMap { a, b ->
                        val order = mapOf(
                            "Poor" to 0,
                            "Bad" to 1,
                            "Good" to 2,
                            "Great" to 3
                        )
                        order[a]!!.compareTo(order[b]!!)
                    })
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

                    if (state.studentProgress.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    }

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

                    if (state.categoryAnalysis.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    }

                    CategoryAnalysisChart(state.categoryAnalysis)
                }
            }

            // Section usage distribution Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Section Usages Distribution",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.sectionUsages.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    }

                    SectionUsageDistributionChart(state.sectionUsages)
                }
            }

            // Section score distribution Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Section Scores Distribution",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.sectionScores.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    }

                    SectionScoreDistributionChart(state.sectionScores)
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
                    StudentProgress("Student 1", 0.5f, 1609459200000L),
                    StudentProgress("Student 2", 0.7f, 1609459200000L),
                    StudentProgress("Student 3", 0.9f, 1609459200000L)
                ),
                categoryAnalysis = listOf(
                    CategoryAnalysis(
                        "Category 1",
                        50f,
                    ),
                    CategoryAnalysis(
                        "Category 2",
                        90f,
                    ),
                    CategoryAnalysis(
                        "Category 3",
                        70f,
                    )
                )
            )
        )
    }
}