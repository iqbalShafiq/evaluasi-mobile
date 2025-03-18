package id.usecase.evaluasi.authentication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val id: String,
    val name: String,
    val email: String
)