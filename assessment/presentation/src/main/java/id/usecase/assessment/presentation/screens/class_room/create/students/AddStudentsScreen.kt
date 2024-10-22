@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create.students

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentCard
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentCardState
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddStudentsScreenRoot(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    openAutoFillScanner: () -> Unit,
    viewModel: AddStudentsViewModel = koinViewModel()
) {
    AddStudentsScreen(
        modifier = modifier,
        onBackPressed = onBackPressed,
        openAutoFillScanner = openAutoFillScanner,
        state = viewModel.state.value,
        onAction = { action ->
            when (action) {
                is AddStudentsAction.AddStudents -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
fun AddStudentsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    openAutoFillScanner: () -> Unit,
    onAction: (AddStudentsAction) -> Unit,
    state: AddStudentsState
) {
    Scaffold(
        topBar = {
            EvaluasiTopAppBar(
                title = "Add Students",
                navigationIcon = ImageVector.vectorResource(R.drawable.rounded_arrow_back),
                onNavigationClicked = onBackPressed
            )
        },
        content = { innerPadding ->
            ConstraintLayout(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                val (content, buttonContainer) = createRefs()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(content) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(buttonContainer.top)
                            height = Dimension.fillToConstraints
                        }
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Students",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val students = remember { mutableStateListOf<AddStudentCardState>() }
                    students.clear()
                    students.addAll(state.studentList)

                    LazyColumn {
                        items(students.size) { index ->
                            val student = students[index]

                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                            ) {
                                AddStudentCard(state = student)

                                if (
                                    index == students.size - 1 &&
                                    student.name.text.isNotEmpty()
                                ) students.add(AddStudentCardState())

                                if (
                                    index != students.size - 1 &&
                                    student.name.text.isEmpty()
                                ) students.removeAt(index)
                            }

                            Spacer(modifier = Modifier.padding(8.dp))
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                        .constrainAs(buttonContainer) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EvaluasiButton(
                        modifier = Modifier.weight(1f),
                        text = "Auto Fill Student",
                        buttonType = ButtonType.INVERSE,
                        onClick = openAutoFillScanner
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    EvaluasiButton(
                        modifier = Modifier.weight(1f),
                        text = "Create Class Room",
                        buttonType = ButtonType.PRIMARY,
                        onClick = {
                            onAction(
                                AddStudentsAction.AddStudents(
                                    state.studentList
                                )
                            )
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun AddStudentsPreview() {
    val state = remember { AddStudentsState() }
    EvaluasiTheme {
        AddStudentsScreen(
            state = state,
            onBackPressed = { },
            openAutoFillScanner = { },
            onAction = { }
        )
    }
}