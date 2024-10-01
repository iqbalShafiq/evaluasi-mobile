@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.students.AddStudentCardState
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.text_field.EvaluasiTextField

@Composable
fun CreateClassRoomScreenRoot(modifier: Modifier = Modifier) {

}

@Composable
fun CreateClassRoomScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    state: CreateClassRoomState
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = "Create Class Room",
                navigationIcon = ImageVector.vectorResource(
                    R.drawable.rounded_arrow_back
                ),
                onNavigationClicked = onBackPressed
            )
        },
        content = { innerPadding ->
            ConstraintLayout(
                modifier = Modifier
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
                    EvaluasiTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Class Name",
                        placeholder = "Type name",
                        state = state.classRoomName,
                        inputType = KeyboardType.Text
                    )

                    EvaluasiTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Subject",
                        placeholder = "Type subject",
                        state = state.subject,
                        inputType = KeyboardType.Text
                    )

                    EvaluasiTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Start Class Date",
                        placeholder = "Pick date",
                        state = state.startDate,
                        inputType = KeyboardType.Text
                    )
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
                        text = "Next",
                        buttonType = ButtonType.PRIMARY,
                        onClick = { }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun CreateClassRoomPreview() {
    EvaluasiTheme {
        CreateClassRoomScreen(
            onBackPressed = { },
            state = CreateClassRoomState(
                classRoomName = TextFieldState(),
                subject = TextFieldState(),
                startDate = TextFieldState(),
                students = listOf(
                    AddStudentCardState(
                        identifier = TextFieldState(),
                        name = TextFieldState()
                    )
                )
            )
        )
    }
}