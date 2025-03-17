package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryNetworkModel(
    val id: Int,
    val name: String,
    val percentage: Double,
    val classRoomId: Int,
    val createdTime: Long,
    val lastModifiedTime: Long
)