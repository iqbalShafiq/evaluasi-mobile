package id.usecase.core.data.sync

import id.usecase.core.data.networking.NetworkResponse
import id.usecase.core.data.networking.post
import id.usecase.core.data.sync.model.AssessmentNetworkModel
import id.usecase.core.data.sync.model.CategoryNetworkModel
import id.usecase.core.data.sync.model.ClassRoomNetworkModel
import id.usecase.core.data.sync.model.EventNetworkModel
import id.usecase.core.data.sync.model.EventSectionNetworkModel
import id.usecase.core.data.sync.model.SectionNetworkModel
import id.usecase.core.data.sync.model.StudentNetworkModel
import id.usecase.core.domain.utils.DataError
import id.usecase.core.domain.utils.Result
import io.ktor.client.HttpClient

class SyncApiService(
    private val httpClient: HttpClient
) {
    suspend fun syncAssessment(request: AssessmentNetworkModel): Result<NetworkResponse<Nothing>, DataError.Network> {
        return httpClient.post<AssessmentNetworkModel, NetworkResponse<Nothing>>(
            route = "sync/assessment",
            body = AssessmentNetworkModel(
                id = request.id,
                studentId = request.studentId,
                eventId = request.eventId,
                score = request.score,
                createdTime = request.createdTime,
                updatedTime = request.updatedTime
            )
        )
    }

    suspend fun syncSection(request: SectionNetworkModel): Result<NetworkResponse<Boolean>, DataError.Network> {
        return httpClient.post<SectionNetworkModel, NetworkResponse<Boolean>>(
            route = "sync/section",
            body = SectionNetworkModel(
                id = request.id,
                name = request.name,
                topics = request.topics,
                classRoomId = request.classRoomId,
                createdTime = request.createdTime,
                updatedTime = request.updatedTime
            )
        )
    }

    suspend fun syncStudent(request: StudentNetworkModel): Result<NetworkResponse<Boolean>, DataError.Network> {
        return httpClient.post<StudentNetworkModel, NetworkResponse<Boolean>>(
            route = "sync/student",
            body = StudentNetworkModel(
                id = request.id,
                name = request.name,
                identifier = request.identifier,
                classRoomId = request.classRoomId,
                createdTime = request.createdTime,
                updatedTime = request.updatedTime
            )
        )
    }

    suspend fun syncClassRoom(request: ClassRoomNetworkModel): Result<NetworkResponse<Boolean>, DataError.Network> {
        return httpClient.post<ClassRoomNetworkModel, NetworkResponse<Boolean>>(
            route = "sync/classRoom",
            body = ClassRoomNetworkModel(
                id = request.id,
                teacherId = request.teacherId,
                name = request.name,
                subject = request.subject,
                lastModifiedStatus = request.lastModifiedStatus,
                createdTime = request.createdTime,
                updatedTime = request.updatedTime,
                description = request.description,
                startPeriod = request.startPeriod,
                longPeriod = request.longPeriod,
                schedule = request.schedule
            )
        )
    }

    suspend fun syncCategory(request: CategoryNetworkModel): Result<NetworkResponse<Boolean>, DataError.Network> {
        return httpClient.post<CategoryNetworkModel, NetworkResponse<Boolean>>(
            route = "sync/category",
            body = CategoryNetworkModel(
                id = request.id,
                name = request.name,
                createdTime = request.createdTime,
                updatedTime = request.updatedTime,
                percentage = request.percentage,
                classRoomId = request.classRoomId
            )
        )
    }

    suspend fun syncEvent(request: EventNetworkModel): Result<NetworkResponse<Boolean>, DataError.Network> {
        return httpClient.post<EventNetworkModel, NetworkResponse<Boolean>>(
            route = "sync/event",
            body = EventNetworkModel(
                id = request.id,
                name = request.name,
                createdTime = request.createdTime,
                updatedTime = request.updatedTime,
                purpose = request.purpose,
                eventDate = request.eventDate,
                categoryId = request.categoryId
            )
        )
    }

    suspend fun syncEventSection(request: EventSectionNetworkModel): Result<NetworkResponse<Boolean>, DataError.Network> {
        return httpClient.post<EventSectionNetworkModel, NetworkResponse<Boolean>>(
            route = "sync/event",
            body = EventSectionNetworkModel(
                id = request.id,
                eventId = request.eventId,
                sectionId = request.sectionId,
            )
        )
    }
}