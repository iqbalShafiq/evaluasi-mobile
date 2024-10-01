@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.models.AlertUi
import id.usecase.assessment.presentation.utils.ignoreHorizontalParentPadding
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar

@Composable
fun ClassRoomScreenRoot(modifier: Modifier = Modifier) {

}

@Composable
fun ClassRoomScreen(
    modifier: Modifier = Modifier,
    state: ClassRoomState
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EvaluasiTopAppBar(
                title = "Class Room Detail",
                navigationIcon = ImageVector.vectorResource(
                    R.drawable.rounded_arrow_back
                ),
                trailingIcon = ImageVector.vectorResource(
                    R.drawable.ic_notification
                ),
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    text = "Recent Alerts",
                    style = MaterialTheme.typography.titleMedium,
                )

                LazyRow(
                    modifier = Modifier.ignoreHorizontalParentPadding(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(state.alerts) { alert ->
                        RecentAlertCard(
                            modifier = if(state.alerts.indexOf(alert) != state.alerts.size - 1) Modifier.padding(end = 16.dp) else Modifier.padding(0.dp),
                            alert = alert,
                            onClicked = { }
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                    text = "Assessment History",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    )
}

@Preview
@Composable
private fun ClassRoomPreview() {
    val state = ClassRoomState(
        alerts = listOf(
            AlertUi(
                studentName = "John Doe",
                studentIdentifier = 1,
                studentAverageScore = 60.7,
                classAverageScore = 70.5,
                detectedDate = "2021-12-12",
                alert = "Student is not active in class"
            ),
            AlertUi(
                studentName = "Jane Doe",
                studentIdentifier = 2,
                studentAverageScore = 80.7,
                classAverageScore = 70.5,
                detectedDate = "2021-12-12",
                alert = "Student is not active in class"
            ),
        )
    )
    EvaluasiTheme {
        ClassRoomScreen(
            state = state
        )
    }
}