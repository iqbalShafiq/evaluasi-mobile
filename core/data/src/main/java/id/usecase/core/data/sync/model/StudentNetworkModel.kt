package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentNetworkModel(
    val id: String,
    val name: String,
    val identifier: Int,
    val classRoomId: String,
    val createdTime: Long,
    val lastModifiedTime: Long
)