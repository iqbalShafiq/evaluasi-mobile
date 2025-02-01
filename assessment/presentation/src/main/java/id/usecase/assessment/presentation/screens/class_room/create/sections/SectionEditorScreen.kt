@file:OptIn(ExperimentalMaterial3Api::class)

package id.usecase.assessment.presentation.screens.class_room.create.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.usecase.assessment.presentation.R
import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SectionCard
import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SectionCardState
import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SubSectionState
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.app_bar.EvaluasiTopAppBar
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.dialog.EvaluasiAlertDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SectionEditorScreenRoot(
    modifier: Modifier = Modifier,
    classRoomId: Int,
    isUpdating: Boolean,
    viewModel: SectionEditorViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    onSectionHasSaved: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var errorMessage by remember {
        mutableStateOf("")
    }
    var showErrorDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(SectionEditorAction.LoadSections(classRoomId))
    }

    ObserveAsEvents(
        flow = viewModel.events
    ) { event ->
        when (event) {
            is SectionEditorEvent.OnErrorOccurred -> {
                errorMessage = event.message
                showErrorDialog = true
            }

            SectionEditorEvent.OnSaveSuccess -> {
                onSectionHasSaved()
            }
        }
    }

    // Show error dialog
    EvaluasiAlertDialog(
        showDialog = showErrorDialog,
        title = "Error Has Occurred",
        message = errorMessage,
        onConfirmation = {
            showErrorDialog = false
            errorMessage = ""
        }
    )

    SectionEditorScreen(
        modifier = modifier,
        classRoomId = classRoomId,
        isUpdating = isUpdating,
        state = state,
        onAction = viewModel::onAction,
        onBackPressed = onBackPressed
    )
}

@Composable
private fun SectionEditorScreen(
    modifier: Modifier = Modifier,
    classRoomId: Int,
    isUpdating: Boolean,
    state: SectionEditorState,
    onAction: (SectionEditorAction) -> Unit,
    onBackPressed: () -> Unit,
) {
    val sections = remember(state.sectionStates) {
        mutableStateListOf<SectionCardState>().apply {
            addAll(state.sectionStates)
        }
    }

    Scaffold(
        topBar = {
            EvaluasiTopAppBar(
                title = if (isUpdating) "Update Sections" else "Add Sections",
                navigationIcon = ImageVector.vectorResource(R.drawable.ic_rounded_arrow_back),
                navigationIconTint = MaterialTheme.colorScheme.onSurface,
                onNavigationClicked = onBackPressed
            )
        },
        content = { innerPadding ->
            val scope = rememberCoroutineScope()
            var progressVisible by remember { mutableStateOf(false) }

            ConstraintLayout(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
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
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .alpha(if (progressVisible) 1f else 0f)
                    )

                    Text(
                        text = "Sections",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        items(sections.size) { index ->
                            val section = sections[index]

                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                            ) {
                                SectionCard(
                                    state = section,
                                    onNameChanged = { name ->
                                        sections[index] = section.copy(
                                            name = name,
                                            isValid = section.name.text.isNotEmpty()
                                        )
                                    },
                                    onTopicsChanged = { subSections ->
                                        sections[index] = section.copy(
                                            subSections = subSections
                                        )
                                    }
                                )

                                if (
                                    index == sections.size - 1 &&
                                    section.name.text.isNotEmpty()
                                ) sections.add(SectionCardState())

                                if (
                                    index != sections.size - 1 &&
                                    section.name.text.isEmpty() &&
                                    section.subSections.all { it.description.text.isEmpty() }
                                ) sections.removeAt(index)
                            }
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EvaluasiButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Save Sections",
                        buttonType = ButtonType.PRIMARY,
                        enabled = sections
                            .takeIf { it.size > 1 }
                            ?.dropLast(1)
                            ?.all { it.isValid } == true,
                        onClick = {
                            scope.launch {
                                progressVisible = true
                                onAction(
                                    SectionEditorAction.Save(
                                        sections = sections
                                            .filterIndexed { index, student ->
                                                index != sections.size - 1 ||
                                                        student.name.text.isNotEmpty()
                                            },
                                        classRoomId = classRoomId
                                    )
                                )
                                progressVisible = false
                            }
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun SectionEditorScreenPreview() {
    val state by remember {
        mutableStateOf(
            SectionEditorState(
                sectionStates = listOf(
                    SectionCardState(
                        subSections = listOf(
                            SubSectionState(
                                description = TextFieldValue("Subsection 1")
                            ),
                            SubSectionState(
                                description = TextFieldValue("Subsection 2")
                            )
                        )
                    )
                )
            )
        )
    }

    EvaluasiTheme {
        SectionEditorScreen(
            modifier = Modifier,
            classRoomId = 1,
            isUpdating = false,
            state = state,
            onAction = {},
            onBackPressed = {}
        )
    }
}