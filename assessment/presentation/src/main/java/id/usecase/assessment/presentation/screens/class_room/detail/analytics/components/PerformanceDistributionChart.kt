package id.usecase.assessment.presentation.screens.class_room.detail.analytics.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun PerformanceDistributionChart(
    distribution: Map<String, Float>
) {
    val chartEntryData = distribution.values.mapIndexed { index, value ->
        Pair(index.toFloat(), value)
    }.toTypedArray()

    Chart(
        chart = columnChart(),
        model = entryModelOf(*chartEntryData),
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}