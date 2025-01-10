package id.usecase.assessment.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.R
import id.usecase.designsystem.EvaluasiTheme

@Composable
fun StatisticsHeader(
    modifier: Modifier = Modifier,
    totalClasses: Int,
    totalStudents: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        StatisticItem(
            modifier = Modifier.weight(1f),
            count = totalClasses,
            label = "Total Classes",
            icon = ImageVector.vectorResource(id = R.drawable.school)
        )
        StatisticItem(
            modifier = Modifier.weight(1f),
            count = totalStudents,
            label = "Total Students",
            icon = ImageVector.vectorResource(id = R.drawable.ic_students)
        )
    }
}

@Composable
fun StatisticItem(
    modifier: Modifier = Modifier,
    count: Int,
    label: String,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsHeaderPreview() {
    EvaluasiTheme {
        StatisticsHeader(
            totalClasses = 5,
            totalStudents = 100
        )
    }
}