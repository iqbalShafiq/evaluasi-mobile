package id.usecase.assessment.presentation.screens.assessment.students

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.model.StudentScoreUi
import id.usecase.designsystem.components.text_field.EvaluasiTextField

@Composable
fun StudentAssessmentCard(
    modifier: Modifier = Modifier,
    state: StudentAssessmentState
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Student ID",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = state.data?.studentId.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column {
                    Text(
                        text = "Student Name",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = state.data?.studentName.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column {
                    Text(
                        text = "Average Score",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = state.data?.avgScore.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            EvaluasiTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                label = "Score",
                placeholder = "Type score",
                state = state.score,
                inputType = KeyboardType.Number,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )

            EvaluasiTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                label = "Comments",
                placeholder = "Type comments (optional)",
                state = state.comments,
                inputType = KeyboardType.Number,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    }
}

@Preview
@Composable
private fun StudentAssessmentCardPreview() {
    StudentAssessmentCard(
        state = StudentAssessmentState(
            data = StudentScoreUi(
                studentId = 1,
                studentName = "John Doe",
                comments = "Good job",
                score = 90.0,
                avgScore = 80.0,
                assessmentId = 1
            )
        )
    )
}