package id.usecase.evaluasi

import kotlinx.serialization.Serializable

@Serializable
internal object Home

@Serializable
internal object CreateClassRoom

@Serializable
internal data class CreateCategories(val classRoomId: Int)

@Serializable
internal data class AddStudents(val classRoomId: Int)