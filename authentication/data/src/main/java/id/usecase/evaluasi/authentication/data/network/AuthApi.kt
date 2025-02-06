package id.usecase.evaluasi.authentication.data.network

import id.usecase.evaluasi.authentication.data.model.LoginRequest
import id.usecase.evaluasi.authentication.data.model.RegisterRequest
import id.usecase.evaluasi.authentication.data.model.TeacherResponse

interface AuthApi {
    suspend fun login(request: LoginRequest): TeacherResponse
    suspend fun register(request: RegisterRequest): TeacherResponse
}