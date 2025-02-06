package id.usecase.evaluasi.authentication.data

import id.usecase.evaluasi.authentication.data.network.AuthApi
import id.usecase.evaluasi.authentication.domain.AuthRepository
import id.usecase.evaluasi.authentication.domain.model.Login
import id.usecase.evaluasi.authentication.domain.model.Register
import id.usecase.evaluasi.authentication.domain.model.Teacher
import io.ktor.client.HttpClient

class AuthRepositoryImpl(private val authApi: AuthApi): AuthRepository {
    override suspend fun login(request: Login): Teacher {
        return authApi.login(request.toRequest()).toDomain()
    }

    override suspend fun register(request: Register): Teacher {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }
}