package id.usecase.evaluasi.authentication.domain

import id.usecase.evaluasi.authentication.domain.model.Login
import id.usecase.evaluasi.authentication.domain.model.Register
import id.usecase.evaluasi.authentication.domain.model.Teacher

interface AuthRepository {
    suspend fun login(request: Login): Teacher
    suspend fun register(request: Register): Teacher
    suspend fun logout()
}