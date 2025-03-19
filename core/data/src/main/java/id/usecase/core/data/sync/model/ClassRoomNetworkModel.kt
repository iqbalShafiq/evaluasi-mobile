package id.usecase.core.data.sync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassRoomNetworkModel(
    val id: String,
    @SerialName("teacher_id")
    val teacherId: String,
    val name: String,
    val subject: String,
    val description: String,
    @SerialName("start_period")
    val startPeriod: Long,
    @SerialName("long_period")
    val longPeriod: Long?,
    val schedule: List<Int> = emptyList(),
    @SerialName("last_modified_status")
    val lastModifiedStatus: Char,
    @SerialName("created_time")
    val createdTime: String,
    @SerialName("updated_time")
    val updatedTime: String
)