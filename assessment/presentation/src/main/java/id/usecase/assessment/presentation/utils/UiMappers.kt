package id.usecase.assessment.presentation.utils

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.text.input.TextFieldValue
import id.usecase.assessment.presentation.model.AddStudentUi
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.class_room.create.categories.components.CategoryItemState
import id.usecase.assessment.presentation.screens.class_room.create.students.item.AddStudentItemState
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.student.Student

fun ClassRoom.toUi(): ClassRoomUi = ClassRoomUi(
    id = id,
    className = name,
    subject = subject,
    lastAssessment = "",
    studentCount = 0,
    startPeriod = startPeriod.toString(),
    endPeriod = longPeriod?.toString() ?: ""
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
    name = TextFieldValue(text = name),
    partPercentage = TextFieldValue(text = percentage.toString())
)

fun AddStudentItemState.toDomainForm(classRoomId: Int) = Student(
    id = 0,
    name = name.text.toString(),
    classRoomId = classRoomId,
    identifier = identifier.text.toString().toInt(),
    createdTime = System.currentTimeMillis(),
    lastModifiedTime = System.currentTimeMillis()
)

fun Student.toItemState() = AddStudentItemState(
    identifier = TextFieldState(initialText = id.toString()),
    name = TextFieldState(initialText = name)
)

fun AddStudentItemState.toDomainForm() = AddStudentUi(
    identifier = identifier.text.toString().toInt(),
    name = name.text.toString()
)

fun Event.toUi(classRoomId: Int, assessedStudent: Int, categoryName: String) = AssessmentEventUi(
    id = id,
    name = name,
    totalAssessment = assessedStudent,
    categoryId = categoryId,
    categoryName = categoryName,
    classId = classRoomId,
    eventDate = eventDate.toString(),
    createdTime = createdTime.toString(),
    lastModifiedTime = lastModifiedTime.toString()
)