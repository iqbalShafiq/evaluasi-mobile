package id.usecase.assessment.domain

import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.section.EventSection
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun upsertEvent(event: Event): DataResult<Event?>
    suspend fun upsertEventSection(eventSections: List<EventSection>): DataResult<Unit>
    fun getEventsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Event>>>
    fun getEventsByCategoryId(categoryId: Int): Flow<DataResult<List<Event>>>
    fun getEventById(eventId: Int): Flow<DataResult<Event?>>
    suspend fun deleteEvent(event: Event)
}