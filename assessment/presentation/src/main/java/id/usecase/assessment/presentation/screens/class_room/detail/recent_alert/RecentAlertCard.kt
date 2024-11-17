package id.usecase.assessment.presentation.screens.class_room.detail.recent_alert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.model.AlertUi

@Composable
fun RecentAlertCard(
    modifier: Modifier = Modifier,
    alert: AlertUi,
    onClicked: (AlertUi) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClicked(alert) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column {
                        Text(
                            text = "Identifier",
                            style = MaterialTheme.typography.labelMedium,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = alert.studentIdentifier.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = "Student Name",
                            style = MaterialTheme.typography.labelMedium,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = alert.studentName,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column {
                        Text(
                            text = "Student / Class Avg Score",
                            style = MaterialTheme.typography.labelMedium,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${alert.studentAverageScore} / ${alert.classAverageScore}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Detected Date",
                            style = MaterialTheme.typography.labelMedium,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = alert.detectedDate,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    Text(
                        text = "Alert",
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = alert.alert,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun RecentAlertCardPreview() {
    RecentAlertCard(
        alert = AlertUi(
            studentName = "John Doe",
            studentIdentifier = 1,
            studentAverageScore = 60.7,
            classAverageScore = 80.0,
            detectedDate = "2021-08-01 00:00:00",
            alert = "Student's score is below class average"
        )
    ) {}
}