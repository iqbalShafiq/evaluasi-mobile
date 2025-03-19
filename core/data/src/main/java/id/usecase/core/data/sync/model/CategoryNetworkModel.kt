package id.usecase.core.data.sync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryNetworkModel(
    val id: String,
    val name: String,
    val percentage: Double,
    @SerialName("class_room_id")
    val classRoomId: String,
    @SerialName("created_time")
    val createdTime: Long,
    @SerialName("updated_time")
    val updatedTime: Long
)