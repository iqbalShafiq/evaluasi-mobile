package id.usecase.assessment.presentation.screens.class_room.detail.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.model.AlertUi
import id.usecase.assessment.presentation.utils.toUi
import id.usecase.core.domain.assessment.model.analytics.LowPerformanceAlert

@Composable
fun PerformanceAlertReport(
    modifier: Modifier = Modifier,
    alerts: List<LowPerformanceAlert>,
    onClicked: (AlertUi) -> Unit
) {
    LazyRow(
        modifier = modifier.heightIn(max = 200.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items = alerts, key = { it.studentName }) { alert ->
            PerformanceAlertCard(
                modifier = modifier.fillParentMaxWidth(1f),
                alert = alert.toUi(),
                onClicked = onClicked
            )
        }
    }
}

@Composable
fun PerformanceAlertCard(
    modifier: Modifier = Modifier,
    alert: AlertUi,
    onClicked: (AlertUi) -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClicked(alert) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with Student Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = alert.studentName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "ID: ${alert.studentIdentifier}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Alert Status Chip
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ) {
                    Text(
                        text = "Need Attention",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            // Score Comparison
            Column {
                Text(
                    text = "Performance",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Score Progress Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { (alert.studentAverageScore / 100f).toFloat() },
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${alert.studentAverageScore}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PerformanceAlertCardPreview() {
    PerformanceAlertCard(
        alert = AlertUi(
            studentIdentifier = 123,
            studentName = "John Doe",
            studentAverageScore = 60.7,
        )
    ) {}
}