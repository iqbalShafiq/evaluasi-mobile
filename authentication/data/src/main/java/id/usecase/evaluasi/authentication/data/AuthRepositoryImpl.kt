package id.usecase.evaluasi.authentication.data

import id.usecase.core.data.networking.post
import id.usecase.core.domain.auth.AuthInfo
import id.usecase.core.domain.auth.SessionStorage
import id.usecase.core.domain.utils.DataError
import id.usecase.core.data.networking.NetworkResponse
import id.usecase.core.domain.utils.Result
import id.usecase.core.domain.utils.map
import id.usecase.evaluasi.authentication.data.model.AuthResponse
import id.usecase.evaluasi.authentication.data.model.RegisterResponse
import id.usecase.evaluasi.authentication.domain.AuthRepository
import id.usecase.evaluasi.authentication.domain.model.Login
import id.usecase.evaluasi.authentication.domain.model.Register
import id.usecase.evaluasi.authentication.domain.model.Teacher
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun login(request: Login): Result<Teacher, DataError.Network> {
        val result = httpClient.post<LoginRequest, NetworkResponse<AuthResponse>>(
            route = "auth",
            body = LoginRequest(
                email = request.email,
                password = request.password
            )
        )

        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.data.accessToken,
                    userId = result.data.data.id
                )
            )
        }

        return result.map { it.data.toTeacher() }
    }

    override suspend fun register(request: Register): Result<Teacher, DataError.Network> {
        val result = httpClient.post<RegisterRequest, NetworkResponse<RegisterResponse>>(
            route = "auth/register",
            body = RegisterRequest(
                name = request.name,
                email = request.email,
                password = request.password
            )
        )

        return result.map { it.data.toTeacher() }
    }

    override suspend fun logout() {
        sessionStorage.clear()
    }
}