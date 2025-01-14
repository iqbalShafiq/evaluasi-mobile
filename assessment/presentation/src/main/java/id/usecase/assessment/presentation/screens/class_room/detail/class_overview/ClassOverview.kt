package id.usecase.assessment.presentation.screens.class_room.detail.class_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomState
import id.usecase.assessment.presentation.screens.class_room.detail.class_overview.components.StatisticItem
import id.usecase.designsystem.EvaluasiTheme
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ClassOverviewTab(
    state: ClassRoomState,
    bottomPadding: Dp = 0.dp
) {
    val categories by remember {
        mutableStateOf(
            state
                .assessmentEvents
                .map { it.categoryName }
                .distinct()
        )
    }

    val performanceTrendData by remember {
        mutableStateOf(
            state.performanceTrendData.map {
                Pair(it.x, it.y)
            }.toTypedArray()
        )
    }

    val categoryDistributionData by remember {
        mutableStateOf(
            state.categoryDistributionData.map {
                Pair(it.x, it.y)
            }.toTypedArray()
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            end = 16.dp,
            start = 16.dp,
            bottom = bottomPadding
        ),
    ) {
        item {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
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
                            icon = R.drawable.ic_analytics
                        )
                    }

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                }
            }

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
                    Chart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        chart = lineChart(
                            lines = listOf(
                                LineChart.LineSpec(
                                    lineColor = MaterialTheme.colorScheme.primary.toArgb(),
                                    lineBackgroundShader = DynamicShaders.fromBrush(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary.copy(alpha = com.patrykandpatrick.vico.core.DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                                MaterialTheme.colorScheme.primary.copy(alpha = com.patrykandpatrick.vico.core.DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                                            )
                                        )
                                    )
                                )
                            ),
                        ),
                        model = entryModelOf(*performanceTrendData),
                        startAxis = rememberStartAxis(
                            title = "Avg Score",
                            valueFormatter = { value, _ -> value.toInt().toString() },
                            itemPlacer = AxisItemPlacer.Vertical.default(
                                maxItemCount = 6
                            )
                        ),
                        bottomAxis = rememberBottomAxis(
                            title = "Month",
                            valueFormatter = { value, _ ->
                                Month.of(value.toInt())
                                    .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            }
                        ),
                    )
                }
            }

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
                    Chart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        chart = columnChart(
                            columns = listOf(
                                LineComponent(
                                    color = MaterialTheme.colorScheme.primary.toArgb(),
                                    thicknessDp = 4f
                                )
                            )
                        ),
                        model = entryModelOf(*categoryDistributionData),
                        startAxis = rememberStartAxis(
                            title = "Score",
                            valueFormatter = { value, _ -> value.toInt().toString() },
                            itemPlacer = AxisItemPlacer.Vertical.default(
                                maxItemCount = 6
                            )
                        ),
                        bottomAxis = rememberBottomAxis(
                            title = "Category",
                            valueFormatter = { value, _ -> categories[value.toInt()] }
                        )
                    )
                }
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
        )
    }
}