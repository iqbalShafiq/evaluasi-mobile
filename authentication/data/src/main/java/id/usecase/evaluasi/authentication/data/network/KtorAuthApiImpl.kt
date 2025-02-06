package id.usecase.evaluasi.authentication.data.network

import id.usecase.evaluasi.authentication.data.model.LoginRequest
import id.usecase.evaluasi.authentication.data.model.RegisterRequest
import id.usecase.evaluasi.authentication.data.model.TeacherResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post

class KtorAuthApiImpl(private val httpClient: HttpClient): AuthApi {
    override suspend fun login(request: LoginRequest): TeacherResponse {
        return httpClient.post<TeacherResponse>("$BASE_URL/login") {
            body = request
        }
    }

    override suspend fun register(request: RegisterRequest): TeacherResponse {
        TODO("Not yet implemented")
    }

    companion object {
        const val BASE_URL = "https://usecase.id/evaluasi/api/"
    }
}