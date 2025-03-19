package id.usecase.core.database.entities

import androidx.room.Entity
import id.usecase.core.domain.utils.EntityPrefix
import id.usecase.core.domain.utils.generateEntityId

@Entity(tableName = "event_section_cross_ref", primaryKeys = ["eventId", "sectionId"])
data class EventSectionCrossRef(
    val eventSectionId: String = generateEntityId(EntityPrefix.EVENT_SECTION),
    val eventId: String,
    val sectionId: String
)