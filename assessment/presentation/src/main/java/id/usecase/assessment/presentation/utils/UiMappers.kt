package id.usecase.assessment.presentation.utils

import id.usecase.assessment.presentation.model.AddStudentUi
import id.usecase.assessment.presentation.model.CategoryUi
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.class_room.create.categories.item.CategoryItemState
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentCardState
import id.usecase.core.domain.assessment.model.classroom.ClassRoom

fun ClassRoom.toUi(): ClassRoomUi = ClassRoomUi(
    id = id,
    className = name,
    subject = students.first().name,
    lastAssessment = students.last().name,
    studentCount = students.size,
    startPeriod = startPeriod.toString(),
    endPeriod = endPeriod?.toString() ?: ""
)

fun CategoryItemState.toUi() = CategoryUi(
    name = name.text.toString(),
    percentage = partPercentage.text.toString().toDouble(),
    description = description.text.toString()
)

fun AddStudentCardState.toUi() = AddStudentUi(
    identifier = identifier.text.toString().toInt(),
    name = name.text.toString()
)