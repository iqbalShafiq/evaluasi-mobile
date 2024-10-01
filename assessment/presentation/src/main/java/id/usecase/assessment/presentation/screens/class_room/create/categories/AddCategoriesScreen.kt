@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create.categories

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import id.usecase.assessment.presentation.R
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCategoriesScreenRoot(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: AddCategoriesViewModel = koinViewModel()
) {
    AddCategoriesScreen(
        modifier = modifier,
        state = viewModel.state.value,
        onBackPressed = onBackPressed,
        onAction = { action ->
            when(action) {
                is AddCategoriesAction.AddCategories -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun AddCategoriesScreen(
    modifier: Modifier = Modifier,
    state: AddCategoriesState,
    onBackPressed: () -> Unit,
    onAction: (AddCategoriesAction) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            EvaluasiTopAppBar(
                title = "Add Categories",
                navigationIcon = ImageVector.vectorResource(
                    id = R.drawable.rounded_arrow_back
                ),
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
                        text = "Category List",
                        style = MaterialTheme.typography.titleSmall,
                    )

                    LazyColumn(
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        items(state.categories) { item ->
                            CategoryCard(state = item)
                            Spacer(modifier = Modifier.height(12.dp))
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
                        text = "Back",
                        buttonType = ButtonType.INVERSE,
                        onClick = onBackPressed
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    EvaluasiButton(
                        modifier = Modifier.weight(1f),
                        text = "Next",
                        buttonType = ButtonType.PRIMARY,
                        onClick = {
                            onAction(
                                AddCategoriesAction.AddCategories(
                                    state.categories
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
private fun AddCategoriesPreview() {
    EvaluasiTheme {
        AddCategoriesScreen(
            state = AddCategoriesState(
                categories = listOf(
                    CategoryItemState(
                        name = rememberTextFieldState(),
                        partPercentage = rememberTextFieldState(),
                        description = rememberTextFieldState()
                    )
                )
            ),
            onBackPressed = { },
            onAction = { }
        )
    }
}