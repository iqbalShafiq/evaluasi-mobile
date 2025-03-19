package id.usecase.core.data.sync

import id.usecase.core.data.networking.EmptyResponse
import id.usecase.core.data.networking.postWithEmptyResult
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
    suspend fun syncAssessment(request: AssessmentNetworkModel): Result<EmptyResponse, DataError.Network> {
        return httpClient.postWithEmptyResult<AssessmentNetworkModel>(
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

    suspend fun syncSection(request: SectionNetworkModel): Result<EmptyResponse, DataError.Network> {
        return httpClient.postWithEmptyResult<SectionNetworkModel>(
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

    suspend fun syncStudent(request: StudentNetworkModel): Result<EmptyResponse, DataError.Network> {
        return httpClient.postWithEmptyResult<StudentNetworkModel>(
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

    suspend fun syncClassRoom(request: ClassRoomNetworkModel): Result<EmptyResponse, DataError.Network> {
        return httpClient.postWithEmptyResult<ClassRoomNetworkModel>(
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

    suspend fun syncCategory(request: CategoryNetworkModel): Result<EmptyResponse, DataError.Network> {
        return httpClient.postWithEmptyResult<CategoryNetworkModel>(
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

    suspend fun syncEvent(request: EventNetworkModel): Result<EmptyResponse, DataError.Network> {
        return httpClient.postWithEmptyResult<EventNetworkModel>(
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

    suspend fun syncEventSection(request: EventSectionNetworkModel): Result<EmptyResponse, DataError.Network> {
        return httpClient.postWithEmptyResult<EventSectionNetworkModel>(
            route = "sync/event",
            body = EventSectionNetworkModel(
                id = request.id,
                eventId = request.eventId,
                sectionId = request.sectionId,
            )
        )
    }
}