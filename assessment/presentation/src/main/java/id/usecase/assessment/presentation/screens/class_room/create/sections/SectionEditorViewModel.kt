package id.usecase.assessment.presentation.screens.class_room.create.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.assessment.domain.SectionRepository
import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SectionCardState
import id.usecase.assessment.presentation.utils.toDomainForm
import id.usecase.assessment.presentation.utils.toItemState
import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SectionEditorViewModel(
    private val sectionRepository: SectionRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _events = Channel<SectionEditorEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(SectionEditorState())
    val state = _state.asStateFlow()

    fun onAction(action: SectionEditorAction) {
        when (action) {
            is SectionEditorAction.LoadSections -> {
                loadSections(action.classRoomId)
            }

            is SectionEditorAction.Save -> {
                saveSection(
                    sectionStates = action.sections,
                    classRoomId = action.classRoomId
                )
            }
        }
    }

    private fun loadSections(classRoomId: Int) {
        viewModelScope.launch(dispatcher) {
            sectionRepository.getSectionsByClassRoomId(classRoomId)
                .catch { e ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(
                        SectionEditorEvent.OnErrorOccurred(
                            message = e.message ?: "Unknown error"
                        )
                    )
                }
                .collectLatest { result ->
                    when (result) {
                        DataResult.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }

                        is DataResult.Success -> {
                            val sectionStates = withContext(Dispatchers.Default) {
                                result.data.map { it.toItemState(classRoomId) }
                            }

                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    sectionStates = sectionStates,
                                    isUpdating = sectionStates.isNotEmpty()
                                )
                            }

                            if (sectionStates.isEmpty()) {
                                _state.update {
                                    it.copy(
                                        sectionStates = listOf(SectionCardState())
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun saveSection(sectionStates: List<SectionCardState>, classRoomId: Int) {
        viewModelScope.launch(dispatcher) {
            try {
                val sections = withContext(Dispatchers.Default) {
                    sectionStates.map { section ->
                        section.toDomainForm(classRoomId)
                    }
                }

                val result = sectionRepository.upsertSection(sections)

                when (result) {
                    DataResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }

                    is DataResult.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        _events.send(SectionEditorEvent.OnSaveSuccess)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _events.send(
                    SectionEditorEvent.OnErrorOccurred(
                        message = e.message ?: "Unknown error"
                    )
                )
            }
        }
    }
}