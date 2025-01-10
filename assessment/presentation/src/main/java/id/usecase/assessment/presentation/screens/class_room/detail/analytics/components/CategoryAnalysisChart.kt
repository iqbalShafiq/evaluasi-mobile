package id.usecase.assessment.presentation.screens.class_room.detail.analytics.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import id.usecase.assessment.presentation.model.CategoryAnalysis

@Composable
fun CategoryAnalysisChart(
    analysis: List<CategoryAnalysis>
) {
    val startAxis = rememberStartAxis(
        valueFormatter = { value, _ -> "${value.toInt()}%" }
    )
    val bottomAxis = rememberBottomAxis(
        valueFormatter = { value, _ ->
            analysis.getOrNull(value.toInt())?.categoryName ?: ""
        }
    )
    val chartEntryData = analysis.map { categoryAnalysis ->
        Pair(categoryAnalysis.totalAssessments.toFloat(), categoryAnalysis.averageScore)
    }.toTypedArray()

    Chart(
        chart = columnChart(),
        model = entryModelOf(*chartEntryData),
        startAxis = startAxis,
        bottomAxis = bottomAxis,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview
@Composable
private fun CategoryAnalysisChartPreview() {
    CategoryAnalysisChart(
        analysis = listOf(
            CategoryAnalysis("Category 1", 10F, 80),
            CategoryAnalysis("Category 2", 5F, 60),
            CategoryAnalysis("Category 3", 7F, 70),
            CategoryAnalysis("Category 4", 3F, 40),
            CategoryAnalysis("Category 5", 8F, 90),
        )
    )
}