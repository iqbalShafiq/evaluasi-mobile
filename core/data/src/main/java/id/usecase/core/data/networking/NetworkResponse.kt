package id.usecase.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse<T>(
    val data: T
)
