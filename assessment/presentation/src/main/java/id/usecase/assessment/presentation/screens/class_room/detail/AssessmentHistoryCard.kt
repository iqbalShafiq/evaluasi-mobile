package id.usecase.assessment.presentation.screens.class_room.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.designsystem.components.button.EvaluasiButton

@Composable
fun AssessmentHistoryCard(
    modifier: Modifier = Modifier,
    eventUi: AssessmentEventUi,
    onDetailClicked: (AssessmentEventUi) -> Unit,
    onAlertClicked: (AssessmentEventUi) -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Event Name",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = eventUi.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_notification),
                    contentDescription = "Alert of assessment",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
                )
            }

            Spacer(
                modifier = Modifier.padding(top = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Column {
                    Text(
                        text = "Taken Date",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = eventUi.eventDate,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.padding(start = 16.dp))

                Column {
                    Text(
                        text = "Students",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = eventUi.assessedStudentCount.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(
                modifier = Modifier.padding(top = 16.dp)
            )

            EvaluasiButton(
                modifier = Modifier.fillMaxWidth(),
                text = "View Details",
                onClick = { onDetailClicked(eventUi) }
            )
        }
    }
}

@Preview
@Composable
private fun AssessmentHistoryCardPreview() {
    AssessmentHistoryCard(
        eventUi = AssessmentEventUi(
            id = 1,
            name = "Assessment 1",
            eventDate = "03 Oct 2024 12:00:00",
            createdTime = "03 Oct 2024 12:00:00",
            assessedStudentCount = 10,
            categoryId = 1,
            categoryName = "Category 1",
            classId = 1,
            lastModifiedTime = "03 Oct 2024 12:00:00",
        ),
        onDetailClicked = {},
        onAlertClicked = {}
    )
}