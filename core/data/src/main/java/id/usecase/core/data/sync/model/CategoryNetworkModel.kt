package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryNetworkModel(
    val id: String,
    val name: String,
    val percentage: Double,
    val classRoomId: String,
    val createdTime: Long,
    val lastModifiedTime: Long
)