package id.usecase.evaluasi.authentication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val teacher: TeacherResponse,
    val token: String
)
