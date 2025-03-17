package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class SectionNetworkModel(
    val id: Int,
    val name: String,
    val topics: List<String>,
    val classRoomId: Int,
    val createdTime: Long,
    val lastModifiedTime: Long
)