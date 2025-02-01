package id.usecase.assessment.presentation.utils

import androidx.compose.ui.text.input.TextFieldValue
import id.usecase.assessment.presentation.model.AddStudentUi
import id.usecase.assessment.presentation.model.AssessmentEventUi
import id.usecase.assessment.presentation.model.ClassRoomUi
import id.usecase.assessment.presentation.screens.class_room.create.categories.components.CategoryItemState
import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SectionCardState
import id.usecase.assessment.presentation.screens.class_room.create.sections.components.SubSectionState
import id.usecase.assessment.presentation.screens.class_room.create.students.components.AddStudentItemState
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.section.Section
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
    id = id,
    name = name.text,
    percentage = partPercentage.text.toDouble(),
    classRoomId = classRoomId,
    createdTime = System.currentTimeMillis(),
    lastModifiedTime = System.currentTimeMillis()
)

fun Category.toItemState() = CategoryItemState(
    id = id,
    name = TextFieldValue(text = name),
    partPercentage = TextFieldValue(text = percentage.toString())
)

fun AddStudentItemState.toDomainForm(classRoomId: Int) = Student(
    id = id,
    name = name.text,
    classRoomId = classRoomId,
    identifier = identifier.text.toInt(),
    createdTime = System.currentTimeMillis(),
    lastModifiedTime = System.currentTimeMillis()
)

fun Student.toItemState() = AddStudentItemState(
    id = id,
    identifier = TextFieldValue(text = id.toString()),
    name = TextFieldValue(text = name),
    isValid = true
)

fun AddStudentItemState.toDomainForm() = AddStudentUi(
    identifier = identifier.text.toInt(),
    name = name.text
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

fun SectionCardState.toDomainForm(classRoomId: Int) = Section(
    id = sectionId,
    name = name.text,
    topics = subSections.map { it.description.text },
    classRoomId = classRoomId,
    createdTime = System.currentTimeMillis(),
    lastModifiedTime = System.currentTimeMillis()
)

fun Section.toItemState() = SectionCardState(
    sectionId = id,
    name = TextFieldValue(text = name),
    subSections = topics.map { SubSectionState(description = TextFieldValue(text = it)) },
    isExpanded = false,
    isValid = true
)