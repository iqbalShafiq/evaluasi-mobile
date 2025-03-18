package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class SectionNetworkModel(
    val id: String,
    val name: String,
    val topics: List<String>,
    val classRoomId: String,
    val createdTime: Long,
    val lastModifiedTime: Long
)