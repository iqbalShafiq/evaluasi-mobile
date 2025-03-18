package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class AssessmentNetworkModel(
    val id: String,
    val studentId: String,
    val eventId: String,
    val score: Double?,
    val createdTime: Long,
    val lastModifiedTime: Long
)