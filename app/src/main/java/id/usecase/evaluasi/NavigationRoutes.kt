package id.usecase.evaluasi

import kotlinx.serialization.Serializable

@Serializable
internal object Home

@Serializable
internal data class CreateClassRoom(val classRoomId: Int? = null)

@Serializable
internal data class CreateCategories(val classRoomId: Int, val isUpdating: Boolean)

@Serializable
internal data class AddStudents(val classRoomId: Int, val isUpdating: Boolean)

@Serializable
internal data class SectionEditor(val classRoomId: Int, val isUpdating: Boolean)

@Serializable
internal data class ClassRoomDetail(val classRoomId: Int)

@Serializable
internal data class AssessmentEventEditor(
    val classRoomId: Int,
    val eventId: Int? = null
)