@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.home.item.ClassRoomCard
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.EvaluasiFloatingActionButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    onClassRoomChosen: (Int) -> Unit,
    onCreateClassRoomClicked: () -> Unit,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    // States
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    // Error dialog state
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Observe events
    ObserveAsEvents(
        flow = homeViewModel.events,
    ) { event ->
        when (event) {
            is HomeEvent.OnErrorOccurred -> {
                errorMessage.value = event.error.message ?: "An error occurred"
            }
        }
    }

    // Render home screen
    HomeScreen(
        modifier = Modifier.fillMaxSize(),
        errorMessage = errorMessage.value,
        classRoomList = state.classRooms,
        onCreateClassRoomClicked = onCreateClassRoomClicked,
        onClassRoomChosen = onClassRoomChosen,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    classRoomList: List<ClassRoomUi>,
    onCreateClassRoomClicked: () -> Unit,
    onClassRoomChosen: (Int) -> Unit
) {
    var fabHeight by remember {
        mutableIntStateOf(0)
    }

    val heightInDp = with(LocalDensity.current) { fabHeight.toDp() }

    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                title = stringResource(R.string.evaluasi)
            )
        },
        floatingActionButton = {
            EvaluasiFloatingActionButton(
                modifier = Modifier.onGloballyPositioned {
                    fabHeight = it.size.height
                },
                text = "Add New Class",
                icon = ImageVector.vectorResource(id = R.drawable.ic_add),
                iconContentDescription = "Add button",
                onClickListener = {
                    onCreateClassRoomClicked()
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                Text(text = "Your class rooms", style = MaterialTheme.typography.titleMedium)

                errorMessage?.let {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = heightInDp + 16.dp)
                ) {
                    items(
                        count = classRoomList.size,
                        key = { index -> classRoomList[index].id },
                        itemContent = { index ->
                            ClassRoomCard(
                                onDetailClickedListener = {
                                    onClassRoomChosen(classRoomList[index].id)
                                },
                                item = classRoomList[index]
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    EvaluasiTheme {
        HomeScreen(
            classRoomList = listOf(
                ClassRoomUi(
                    subject = "Math",
                    className = "A",
                    studentCount = 20,
                    id = 1,
                    lastAssessment = "January Math Exam",
                    startPeriod = "2021-01-01",
                    endPeriod = "2021-12-31"
                ),
                ClassRoomUi(
                    subject = "Science",
                    className = "B",
                    studentCount = 30,
                    id = 2,
                    lastAssessment = "February Science Exam",
                    startPeriod = "2021-01-01",
                    endPeriod = "2021-12-31"
                ),
                ClassRoomUi(
                    subject = "English",
                    className = "C",
                    studentCount = 40,
                    id = 3,
                    lastAssessment = "March English Exam",
                    startPeriod = "2021-01-01",
                    endPeriod = "2021-12-31"
                ),
                ClassRoomUi(
                    subject = "History",
                    className = "D",
                    studentCount = 50,
                    id = 4,
                    lastAssessment = "April History Exam",
                    startPeriod = "2021-01-01",
                    endPeriod = "2021-12-31"
                )
            ),
            onCreateClassRoomClicked = { },
            onClassRoomChosen = { }
        )
    }
}