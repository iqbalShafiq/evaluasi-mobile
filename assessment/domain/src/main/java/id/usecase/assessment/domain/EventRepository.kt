package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.assessment.event.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun upsertEvent(event: Event): DataResult<Event?>
    fun getEventsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Event>>>
    fun getEventsByCategoryId(categoryId: Int): Flow<DataResult<List<Event>>>
    fun getEventById(eventId: Int): Flow<DataResult<Event?>>
    suspend fun deleteEvent(event: Event)
}