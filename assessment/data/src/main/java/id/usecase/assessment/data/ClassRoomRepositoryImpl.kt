package id.usecase.assessment.data

import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ClassRoomRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : ClassRoomRepository {
    override suspend fun upsertClassRoom(classRoom: ClassRoom): DataResult<ClassRoom?> {
        return withContext(dispatcher) {
            try {
                val classRoom = dataSource.upsertClassRoom(classRoom)
                return@withContext DataResult.Success(classRoom)
            } catch (e: Exception) {
                throw e
                return@withContext DataResult.Error(e)
            }
        }
    }

    override fun getClassRooms(): Flow<DataResult<List<ClassRoom>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val classRooms: List<ClassRoom> = dataSource.getClassRooms()
                if (classRooms.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any class room yet")
                        )
                    )
                    return@flow
                }

                emit(DataResult.Success(classRooms))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getClassRoomById(classRoomId: Int): Flow<DataResult<ClassRoom?>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val classRoom: ClassRoom? = dataSource.getClassRoomById(classRoomId)
                if (classRoom == null) {
                    emit(
                        DataResult.Error(
                            Exception("Class room not found")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(classRoom))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteClassRoom(classRoom: ClassRoom): DataResult<Unit> {
        return withContext(dispatcher) {
            try {
                dataSource.deleteClassRoom(classRoom)
                return@withContext DataResult.Success(Unit)
            } catch (e: Exception) {
                throw e
                return@withContext DataResult.Error(e)
            }
        }
    }
}