package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class AssessmentNetworkModel(
    val id: Int,
    val studentId: Int,
    val eventId: Int,
    val score: Double?,
    val createdTime: Long,
    val lastModifiedTime: Long
)