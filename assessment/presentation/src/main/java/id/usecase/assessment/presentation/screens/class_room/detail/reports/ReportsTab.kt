package id.usecase.assessment.presentation.screens.class_room.detail.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.screens.class_room.detail.ClassRoomState
import id.usecase.assessment.presentation.screens.class_room.detail.components.EmptyDataText
import id.usecase.assessment.presentation.screens.class_room.detail.reports.components.PerformanceAlertReport
import id.usecase.assessment.presentation.screens.class_room.detail.reports.components.StudentProgressList
import id.usecase.core.domain.assessment.model.analytics.LowPerformanceAlert
import id.usecase.designsystem.EvaluasiTheme

@Composable
fun ReportsTab(
    state: ClassRoomState,
    bottomPadding: Dp = 0.dp
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = bottomPadding
        )
    ) {
        item {
            PerformanceAlertReport(
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                alerts = state.lowPerformanceAlerts
            ) { }

            // Student Progress Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Student Progress",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.studentProgress.isEmpty()) {
                        EmptyDataText()
                        return@ElevatedCard
                    }

                    StudentProgressList(state.studentProgress)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportsTabPreview() {
    EvaluasiTheme {
        ReportsTab(
            state = ClassRoomState(
                lowPerformanceAlerts = listOf(
                    LowPerformanceAlert(
                        studentName = "John Doe",
                        lastUpdated = System.currentTimeMillis(),
                        averageScore = 50F,
                        studentIdentifier = 1
                    ),
                    LowPerformanceAlert(
                        studentName = "John Duo",
                        lastUpdated = System.currentTimeMillis(),
                        averageScore = 20F,
                        studentIdentifier = 2
                    ),
                    LowPerformanceAlert(
                        studentName = "John asdsa",
                        lastUpdated = System.currentTimeMillis(),
                        averageScore = 20F,
                        studentIdentifier = 2
                    ),
                    LowPerformanceAlert(
                        studentName = "John sdasdDuo",
                        lastUpdated = System.currentTimeMillis(),
                        averageScore = 20F,
                        studentIdentifier = 2
                    ),
                    LowPerformanceAlert(
                        studentName = "John Ddsfsdfuo",
                        lastUpdated = System.currentTimeMillis(),
                        averageScore = 20F,
                        studentIdentifier = 2
                    ),
                ),
                studentProgress = emptyList()
            )
        )
    }
}