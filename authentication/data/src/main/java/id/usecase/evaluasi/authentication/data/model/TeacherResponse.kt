package id.usecase.evaluasi.authentication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TeacherResponse(
    val id: Int,
    val name: String,
    val email: String
)
