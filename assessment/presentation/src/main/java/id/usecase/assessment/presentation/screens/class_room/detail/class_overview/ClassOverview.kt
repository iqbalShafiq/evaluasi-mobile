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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomState
import id.usecase.assessment.presentation.screens.class_room.detail.class_overview.components.StatisticItem
import id.usecase.assessment.presentation.screens.class_room.detail.components.EmptyDataText
import id.usecase.designsystem.EvaluasiTheme
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import java.util.Map.entry

@Composable
fun ClassOverviewTab(
    state: ClassRoomState,
    bottomPadding: Dp = 0.dp
) {
    val performanceModelProducer = remember { CartesianChartModelProducer() }
    val categoryModelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        performanceModelProducer.runTransaction {
            lineSeries {
                series(
                    x = state.performanceTrendData.map { it.first },
                    y = state.performanceTrendData.map { it.second }
                )
            }
        }

        categoryModelProducer.runTransaction {
            columnSeries {
                series(
                    x = state.categoryDistributionData.map { it.first },
                    y = state.categoryDistributionData.map { it.second }
                )
            }
        }
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

                    if (state.assessmentEvents.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    }

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
                            value = String.format(
                                Locale.getDefault(),
                                "%.2f",
                                state.classAverage
                            ),
                            icon = R.drawable.ic_analytics
                        )
                    }

                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
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

                    if (state.performanceTrendData.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    } else {
                        CartesianChartHost(
                            chart = rememberCartesianChart(
                                rememberLineCartesianLayer(),
                                startAxis = VerticalAxis.rememberStart(
                                    valueFormatter = { _, value, _ -> value.toString() },
                                    itemPlacer = VerticalAxis.ItemPlacer.count(count = { 6 }),
                                    tick = rememberAxisTickComponent(
                                        fill = Fill(
                                            shaderProvider = ShaderProvider.verticalGradient(
                                                colors = intArrayOf(
                                                    MaterialTheme.colorScheme.primary
                                                        .copy(alpha = 0.4F)
                                                        .toArgb(),
                                                    MaterialTheme.colorScheme.primary
                                                        .copy(alpha = 0.8F)
                                                        .toArgb()
                                                )
                                            )
                                        )
                                    )
                                ),
                                bottomAxis = HorizontalAxis.rememberBottom(
                                    valueFormatter = { _, value, _ ->
                                        state.performanceTrendData.getOrNull(value.toInt())?.first?.toInt()
                                            ?.let { Month.of(it) }
                                            ?.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                            ?: ""
                                    }
                                )
                            ),
                            modelProducer = performanceModelProducer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }

                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
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

                    if (state.categoryDistributionData.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    } else {
                        CartesianChartHost(
                            chart = rememberCartesianChart(
                                rememberColumnCartesianLayer(),
                                startAxis = VerticalAxis.rememberStart(
                                    valueFormatter = { _, value, _ -> value.toString() },
                                    itemPlacer = VerticalAxis.ItemPlacer.count(count = { 6 })
                                ),
                                bottomAxis = HorizontalAxis.rememberBottom(
                                    valueFormatter = { _, value, _ ->
                                        state.categoryList.getOrNull(value.toInt()) ?: ""
                                    }
                                )
                            ),
                            modelProducer = categoryModelProducer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                        )
                    }

                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
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
                    Pair(1f, 50f),
                    Pair(2f, 60f),
                    Pair(3f, 70f),
                    Pair(4f, 20f),
                    Pair(5f, 30f),
                    Pair(6f, 40f),
                ),
                categoryDistributionData = listOf(
                    Pair(0f, 20f),
                    Pair(1f, 30f),
                    Pair(2f, 50f),
                ),
                categoryList = listOf("Monthly", "Mid", "Final")
            )
        )
    }
}