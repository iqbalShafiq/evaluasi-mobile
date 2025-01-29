package id.usecase.core.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import id.usecase.core.database.entities.EventEntity
import id.usecase.core.database.entities.EventSectionCrossRef
import id.usecase.core.database.entities.SectionEntity

data class EventWithSections(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(EventSectionCrossRef::class)
    )
    val sections: List<SectionEntity>
)
