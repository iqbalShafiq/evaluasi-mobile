package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class ClassRoomNetworkModel(
    val id: Int,
    val name: String,
    val subject: String,
    val description: String,
    val startPeriod: Long,
    val longPeriod: Long?,
    val createdTime: Long,
    val lastModifiedTime: Long,
    val schedule: List<Int> = emptyList()
)