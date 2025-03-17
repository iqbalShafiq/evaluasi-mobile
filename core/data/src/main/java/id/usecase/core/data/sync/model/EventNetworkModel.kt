package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class EventNetworkModel(
    val id: Int,
    val name: String,
    val purpose: String,
    val eventDate: Long,
    val categoryId: Int,
    val createdTime: Long,
    val lastModifiedTime: Long
)