package id.usecase.assessment.presentation.screens.class_room.detail.class_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomState
import id.usecase.assessment.presentation.screens.class_room.detail.class_overview.components.StatisticItem
import id.usecase.designsystem.EvaluasiTheme

@Composable
fun ClassOverviewTab(state: ClassRoomState) {
    val scrollState = rememberScrollState()
    val performanceTrendData = state.performanceTrendData.map {
        Pair(it.x, it.y)
    }.toTypedArray()
    val categoryDistributionData = state.categoryDistributionData.map {
        Pair(it.x, it.y)
    }.toTypedArray()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp, start = 16.dp, top = 16.dp)
            .verticalScroll(scrollState)
    ) {
        // Class Statistics Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Class Statistics",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Statistics Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatisticItem(
                        title = "Students",
                        value = state.totalStudents.toString(),
                        icon = R.drawable.ic_students
                    )
                    StatisticItem(
                        title = "Assessments",
                        value = state.assessmentEvents.size.toString(),
                        icon = R.drawable.ic_edit
                    )
                    StatisticItem(
                        title = "Avg Score",
                        value = "${state.classAverage}%",
                        icon = R.drawable.ic_information
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        // Performance Trend Chart
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Class Performance Trend",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Line Chart for performance trend
                Chart(
                    chart = lineChart(),
                    model = entryModelOf(*performanceTrendData),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }

        // Category Distribution Chart
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Assessment Categories",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Column Chart for category distribution
                Chart(
                    chart = columnChart(),
                    model = entryModelOf(*categoryDistributionData),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassOverviewPreview() {
    EvaluasiTheme {
        ClassOverviewTab(
            state = ClassRoomState(
                totalStudents = 30,
                assessmentEvents = emptyList(),
                classAverage = 80.0,
                performanceTrendData = emptyList(),
                categoryDistributionData = emptyList()
            )
        )
    }
}