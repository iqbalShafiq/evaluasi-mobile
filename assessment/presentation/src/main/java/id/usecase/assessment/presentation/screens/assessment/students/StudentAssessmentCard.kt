package id.usecase.assessment.presentation.screens.assessment.students

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.data?.studentName.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "ID: ${state.data?.studentId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Average Score with emphasize
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Average",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${state.data?.avgScore}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if ((state.data?.avgScore ?: 0.0) >= 75) {
                            MaterialTheme.colorScheme.primary
                        } else MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score input with better visual hierarchy
            EvaluasiTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Assessment Score",
                placeholder = "Enter score (0-100)",
                value = state.score,
                onValueChange = {  },
            )

            Spacer(modifier = Modifier.height(8.dp))

            EvaluasiTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Comments",
                placeholder = "Add comments (optional)",
                value = state.comments,
                onValueChange = {  }
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