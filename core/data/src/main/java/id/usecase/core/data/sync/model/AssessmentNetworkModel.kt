package id.usecase.core.data.sync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssessmentNetworkModel(
    val id: String,
    @SerialName("student_id")
    val studentId: String,
    @SerialName("event_id")
    val eventId: String,
    val score: Double?,
    @SerialName("created_time")
    val createdTime: Long,
    @SerialName("updated_time")
    val updatedTime: Long
)