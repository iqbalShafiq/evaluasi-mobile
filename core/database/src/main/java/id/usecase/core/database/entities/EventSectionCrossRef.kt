package id.usecase.core.database.entities

import androidx.room.Entity

@Entity(tableName = "event_section_cross_ref", primaryKeys = ["eventId", "sectionId"])
data class EventSectionCrossRef(
    val eventId: Int,
    val sectionId: Int
)