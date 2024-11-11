package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import kotlinx.coroutines.flow.Flow

interface ClassRoomRepository {
    suspend fun upsertClassRoom(classRoom: ClassRoom): DataResult<ClassRoom?>
    fun getClassRooms(): Flow<DataResult<List<ClassRoom>>>
    fun getClassRoomById(classRoomId: Int): Flow<DataResult<ClassRoom?>>
    suspend fun deleteClassRoom(classRoom: ClassRoom): DataResult<Unit>
}