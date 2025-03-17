package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentNetworkModel(
    val id: Int,
    val name: String,
    val identifier: Int,
    val classRoomId: Int,
    val createdTime: Long,
    val lastModifiedTime: Long
)