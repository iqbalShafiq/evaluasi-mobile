package id.usecase.core.data.sync.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EventSectionNetworkModel(
    val id: String = UUID.randomUUID().toString(),
    val eventId: String,
    val sectionId: String,
)