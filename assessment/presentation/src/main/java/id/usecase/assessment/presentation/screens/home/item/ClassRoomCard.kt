package id.usecase.assessment.presentation.screens.home.item

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.designsystem.components.button.EvaluasiButton

@Composable
fun ClassRoomCard(
    modifier: Modifier = Modifier,
    onDetailClickedListener: (ClassRoomUi) -> Unit,
    item: ClassRoomUi
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onDetailClickedListener(item) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = item.subject,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )

                        Row {
                            Text(
                                text = "Class ${item.className}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "-",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "${item.studentCount} students",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_notification
                        ),
                        contentDescription = "Notification",
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Start Period",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = item.startPeriod,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "End Period",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = item.endPeriod,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Last Assessment",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = item.lastAssessment,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                EvaluasiButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Class Detail",
                    onClick = { onDetailClickedListener(item) }
                )
            }
        }
    )
}

@Preview
@Composable
private fun ClassRoomCardPreview() {
    val classRoom = ClassRoomUi(
        id = 1,
        className = "1A",
        subject = "Subject 1",
        lastAssessment = "Lorem ipsum dolor kucing mengionglah meow dolor",
        studentCount = 48,
        startPeriod = "2021-01-01",
        endPeriod = "2021-12-31"
    )

    ClassRoomCard(
        item = classRoom,
        onDetailClickedListener = { /*TODO*/ }
    )
}