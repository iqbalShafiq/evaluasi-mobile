package id.usecase.core.data.sync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventNetworkModel(
    val id: String,
    val name: String,
    val purpose: String,
    @SerialName("event_date")
    val eventDate: Long,
    @SerialName("category_id")
    val categoryId: String,
    @SerialName("created_time")
    val createdTime: Long,
    @SerialName("updated_time")
    val updatedTime: Long
)