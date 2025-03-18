package id.usecase.assessment.data

import id.usecase.assessment.domain.EventRepository
import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.section.EventSection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class EventRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : EventRepository {
    override suspend fun upsertEvent(event: Event): DataResult<Event?> {
        return withContext(dispatcher) {
            val eventId = dataSource.upsertEvent(event)
            DataResult.Success(dataSource.getEventById(eventId))
        }
    }

    override suspend fun upsertEventSection(eventSections: List<EventSection>): DataResult<Unit> {
        return withContext(dispatcher) {
            dataSource.upsertEventSection(eventSections)
            DataResult.Success(Unit)
        }
    }

    override fun getEventsByClassRoomId(classRoomId: String): Flow<DataResult<List<Event>>> {
        return flow {
            emit(DataResult.Loading)
            val events: List<Event> = dataSource.getEventsByClassRoomId(classRoomId)
            emit(DataResult.Success(events))
        }.flowOn(dispatcher)
    }

    override fun getEventsByCategoryId(categoryId: String): Flow<DataResult<List<Event>>> {
        return flow {
            emit(DataResult.Loading)
            val events: List<Event> = dataSource.getEventsByCategoryId(categoryId)
            emit(DataResult.Success(events))
        }.flowOn(dispatcher)
    }

    override fun getEventById(eventId: String): Flow<DataResult<Event?>> {
        return flow {
            emit(DataResult.Loading)
            val event: Event = dataSource.getEventById(
                eventId
            ) ?: throw Exception("Event not found")

            emit(DataResult.Success(event))
        }.flowOn(dispatcher)
    }

    override suspend fun deleteEvent(event: Event) {
        withContext(dispatcher) {
            dataSource.deleteEvent(event)
        }
    }
}