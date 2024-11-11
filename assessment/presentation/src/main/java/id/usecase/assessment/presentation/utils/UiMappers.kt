package id.usecase.assessment.presentation.utils

import androidx.compose.foundation.text.input.TextFieldState
import id.usecase.assessment.presentation.model.AddStudentUi
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.class_room.create.categories.item.CategoryItemState
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentCardState
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.classroom.ClassRoom

fun ClassRoom.toDomainForm(): ClassRoomUi = ClassRoomUi(
    id = id,
    className = name,
    subject = subject,
    lastAssessment = "",
    studentCount = 0,
    startPeriod = startPeriod.toString(),
    endPeriod = endPeriod?.toString() ?: ""
)

fun CategoryItemState.toDomainForm(classRoomId: Int) = Category(
    id = 0,
    name = name.text.toString(),
    percentage = partPercentage.text.toString().toDouble(),
    classRoomId = classRoomId,
    createdTime = System.currentTimeMillis(),
    lastModifiedTime = System.currentTimeMillis()
)

fun Category.toItemState() = CategoryItemState(
    name = TextFieldState(initialText = name),
    partPercentage = TextFieldState(initialText = percentage.toString())
)

fun AddStudentCardState.toDomainForm() = AddStudentUi(
    identifier = identifier.text.toString().toInt(),
    name = name.text.toString()
)