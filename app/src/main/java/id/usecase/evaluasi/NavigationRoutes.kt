package id.usecase.evaluasi

import kotlinx.serialization.Serializable

@Serializable
internal object Login

@Serializable
internal object Register

@Serializable
internal object Home

@Serializable
internal data class CreateClassRoom(val classRoomId: String? = null)

@Serializable
internal data class CreateCategories(val classRoomId: String, val isUpdating: Boolean)

@Serializable
internal data class AddStudents(val classRoomId: String, val isUpdating: Boolean)

@Serializable
internal data class SectionEditor(val classRoomId: String, val isUpdating: Boolean)

@Serializable
internal data class ClassRoomDetail(val classRoomId: String)

@Serializable
internal data class AssessmentEventEditor(
    val classRoomId: String,
    val eventId: String? = null
)