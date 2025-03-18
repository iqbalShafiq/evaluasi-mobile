package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class EventNetworkModel(
    val id: String,
    val name: String,
    val purpose: String,
    val eventDate: Long,
    val categoryId: String,
    val createdTime: Long,
    val lastModifiedTime: Long
)