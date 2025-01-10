package id.usecase.assessment.presentation.screens.class_room.detail.assessment_history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.core.presentation.ui.formatDate
import id.usecase.core.presentation.ui.formatTimeAgo

@Composable
fun AssessmentSummaryItem(
    modifier: Modifier = Modifier,
    assessment: AssessmentEventUi,
    onDetailClicked: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onDetailClicked,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with Assessment Name and Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = assessment.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = assessment.categoryName,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                IconButton(onClick = onDetailClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "View Details",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Assessment Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Date Info
                AssessmentInfoColumn(
                    icon = Icons.Rounded.DateRange,
                    label = "Date",
                    value = formatDate(assessment.eventDate)
                )

                // Total Participants
                AssessmentInfoColumn(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_students),
                    label = "Participants",
                    value = assessment.totalAssessment.toString()
                )

                // Last Modified
                AssessmentInfoColumn(
                    icon = Icons.Rounded.DateRange,
                    label = "Last Modified",
                    value = formatTimeAgo(assessment.lastModifiedTime)
                )
            }

            // Progress Indicator (if needed)
            if (assessment.isInProgress) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { assessment.completionProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Preview
@Composable
private fun AssessmentSummaryItemPreview() {
    AssessmentSummaryItem(
        assessment = AssessmentEventUi(
            id = 1,
            name = "Assessment Name",
            categoryName = "Category Name",
            eventDate = "2021-09-01 00:00:00",
            totalAssessment = 10,
            lastModifiedTime = "2021-09-01 00:00:00",
            isInProgress = false,
            completionProgress = 0.5f,
            categoryId = 1,
            classId = 1,
            createdTime = "2021-09-01 00:00:00"
        ),
        onDetailClicked = { }
    )
}