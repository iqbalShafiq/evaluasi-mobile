package id.usecase.assessment.presentation.screens.class_room.detail.analytics.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import id.usecase.core.domain.assessment.model.analytics.CategoryAnalysis
import java.util.Map.entry

@Composable
fun CategoryAnalysisChart(
    analysis: List<CategoryAnalysis>
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                analysis.forEachIndexed { index, categoryAnalysis ->
                    entry(index.toFloat(), categoryAnalysis.averageScore)
                }
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = { _, value, _ -> value.toString() },
                itemPlacer = VerticalAxis.ItemPlacer.count(count = { 6 })
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = { _, value, _ ->
                    analysis.getOrNull(value.toInt())?.categoryName ?: ""
                }
            )
        ),
        modelProducer = modelProducer,
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