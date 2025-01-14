package id.usecase.assessment.presentation.screens.class_room.create

import androidx.compose.ui.text.input.TextFieldValue
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentItemState
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import java.time.DayOfWeek

data class CreateClassRoomState(
    val classRoom: ClassRoom? = null,
    val classRoomName: TextFieldValue = TextFieldValue(),
    val subject: TextFieldValue = TextFieldValue(),
    val startDate: TextFieldValue = TextFieldValue(),
    val longPeriod: TextFieldValue = TextFieldValue(),
    val description: TextFieldValue = TextFieldValue(),
    val students: List<AddStudentItemState> = emptyList(),
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val hasSchedule: Boolean = false,
    val selectedDays: Set<DayOfWeek> = emptySet()
)
