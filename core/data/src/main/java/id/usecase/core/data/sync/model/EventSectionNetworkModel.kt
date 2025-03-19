package id.usecase.core.data.sync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EventSectionNetworkModel(
    val id: String = UUID.randomUUID().toString(),
    @SerialName("event_id")
    val eventId: String,
    @SerialName("section_id")
    val sectionId: String,
)