package id.usecase.assessment.data

import id.usecase.assessment.domain.EventRepository
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.event.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class EventRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : EventRepository {
    override suspend fun upsertEvent(event: Event) {
        withContext(dispatcher) {
            try {
                dataSource.upsertEvent(event)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getEventsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Event>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val events: List<Event> = dataSource.getEventsByClassRoomId(classRoomId)
                if (events.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any event yet")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(events))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getEventsByCategoryId(categoryId: Int): Flow<DataResult<List<Event>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val events: List<Event> = dataSource.getEventsByCategoryId(categoryId)
                if (events.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any event yet")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(events))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getEventById(eventId: Int): Flow<DataResult<Event?>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val event: Event? = dataSource.getEventById(eventId)
                if (event == null) {
                    emit(
                        DataResult.Error(
                            Exception("Event not found")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(event))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteEvent(event: Event) {
        withContext(dispatcher) {
            try {
                dataSource.deleteEvent(event)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}