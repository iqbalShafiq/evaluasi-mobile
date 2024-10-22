@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.assessment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.model.StudentScoreUi
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentCard
import id.usecase.assessment.presentation.screens.assessment.students.StudentAssessmentState
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.text_field.EvaluasiTextField

@Composable
fun AssessmentScreenRoot(modifier: Modifier = Modifier) {

}

@Composable
fun AssessmentScreen(
    modifier: Modifier = Modifier,
    state: AssessmentState
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                title = stringResource(R.string.create_assessment),
                navigationIcon = ImageVector.vectorResource(
                    R.drawable.rounded_arrow_back
                )
            )
        },
        content = { innerPadding ->
            ConstraintLayout(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                val (content, button) = createRefs()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(content) {
                            top.linkTo(parent.top)
                            bottom.linkTo(button.top)
                            height = Dimension.fillToConstraints
                        }
                ) {
                    EvaluasiTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Assessment Name",
                        placeholder = "Type name",
                        state = state.assessmentName,
                        inputType = KeyboardType.Text
                    )

                    EvaluasiTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Assessment Date",
                        placeholder = "Pick date",
                        state = state.assessmentName,
                        inputType = KeyboardType.Text
                    )

                    EvaluasiTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Category",
                        placeholder = "Choose category",
                        state = state.assessmentName,
                        inputType = KeyboardType.Text
                    )

                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "Students",
                        style = MaterialTheme.typography.labelMedium
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 8.dp)
                    ) {
                        items(state.students) { student ->
                            StudentAssessmentCard(
                                state = student
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                EvaluasiButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(button) {
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(vertical = 12.dp),
                    text = "Save Assessment",
                    onClick = { /* TODO: Save assessment */ },
                    buttonType = ButtonType.INVERSE
                )
            }
        }
    )
}

@Preview
@Composable
private fun AssessmentScreenPreview() {
    EvaluasiTheme {
        AssessmentScreen(
            state = AssessmentState(
                students = listOf(
                    StudentAssessmentState(
                        data = StudentScoreUi(
                            studentId = 1,
                            studentName = "John Doe",
                            comments = "Good job",
                            score = 90.0,
                            avgScore = 80.0
                        )
                    ),
                    StudentAssessmentState(
                        data = StudentScoreUi(
                            studentId = 2,
                            studentName = "Jane Doe",
                            comments = "Good job",
                            score = 90.0,
                            avgScore = 80.0
                        )
                    ),
                    StudentAssessmentState(
                        data = StudentScoreUi(
                            studentId = 3,
                            studentName = "John Smith",
                            comments = "Good job",
                            score = 90.0,
                            avgScore = 80.0
                        )
                    )
                )
            )
        )
    }
}