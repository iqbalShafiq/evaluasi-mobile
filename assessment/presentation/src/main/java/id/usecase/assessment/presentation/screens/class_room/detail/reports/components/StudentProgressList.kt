package id.usecase.assessment.presentation.screens.class_room.detail.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import id.usecase.core.domain.assessment.model.analytics.StudentProgress
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun StudentProgressList(progress: List<StudentProgress>) {
    LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
        items(items = progress, key = { it.studentName }) { studentProgress ->
            StudentProgressItem(studentProgress)
            HorizontalDivider()
        }
    }
}

@Composable
fun StudentProgressItem(progress: StudentProgress) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = progress.studentName,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Last Assessment: ${
                    SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    ).format(progress.lastUpdated)
                }",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        LinearProgressIndicator(
            progress = { progress.progressPercentage },
            modifier = Modifier
                .width(100.dp)
                .height(8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
        )
    }
}