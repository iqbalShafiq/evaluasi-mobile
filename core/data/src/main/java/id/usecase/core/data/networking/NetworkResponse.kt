package id.usecase.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse<T>(
    val data: T
)

@Serializable
data class EmptyResponse(
    val data: Nothing? = null
)
