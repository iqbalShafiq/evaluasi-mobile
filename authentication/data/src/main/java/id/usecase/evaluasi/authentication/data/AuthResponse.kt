package id.usecase.evaluasi.authentication.data

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val id: Int,
    val name: String,
    val email: String,
    val accessToken: String
)
