package id.usecase.assessment.presentation.screens.class_room.detail.analytics.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf
import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import id.usecase.core.domain.assessment.model.analytics.SectionScore

@Composable
fun SectionScoreDistributionChart(
    analysis: List<SectionScore>
) {
    val chartEntryData = analysis.mapIndexed { index, sectionScore ->
        Pair(index.toFloat(), sectionScore.averageScore)
    }.toTypedArray()

    Chart(
        chart = columnChart(
            columns = listOf(
                LineComponent(
                    color = MaterialTheme.colorScheme.primary.toArgb(),
                    thicknessDp = 4f
                )
            )
        ),
        model = entryModelOf(*chartEntryData),
        startAxis = rememberStartAxis(
            valueFormatter = { value, _ -> "${value.toInt()}" },
            itemPlacer = AxisItemPlacer.Vertical.default(
                maxItemCount = 6
            )
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                analysis.getOrNull(value.toInt())?.sectionName ?: ""
            }
        ),
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
            CategoryAnalysis("Category 1", 10F),
            CategoryAnalysis("Category 2", 5F),
            CategoryAnalysis("Category 3", 7F),
            CategoryAnalysis("Category 4", 3F),
            CategoryAnalysis("Category 5", 8F),
        )
    )
}