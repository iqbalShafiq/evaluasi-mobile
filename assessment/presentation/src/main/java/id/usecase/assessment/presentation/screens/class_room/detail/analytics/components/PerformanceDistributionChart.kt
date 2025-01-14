package id.usecase.assessment.presentation.screens.class_room.detail.analytics.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun PerformanceDistributionChart(
    distribution: Map<String, Float>
) {
    val chartEntryData = distribution.values.mapIndexed { index, value ->
        Pair(index.toFloat(), value)
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
            title = "Score",
            valueFormatter = { value, _ -> value.toString() },
            itemPlacer = AxisItemPlacer.Vertical.default(
                maxItemCount = 6
            )
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                distribution.toList().getOrNull(value.toInt())?.first ?: ""
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}