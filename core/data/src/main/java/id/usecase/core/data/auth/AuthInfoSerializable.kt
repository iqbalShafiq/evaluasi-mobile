package id.usecase.core.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    @SerialName("token")
    val accessToken: String,
    val userId: String
)
