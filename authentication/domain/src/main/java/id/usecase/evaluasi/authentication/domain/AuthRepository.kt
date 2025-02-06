package id.usecase.evaluasi.authentication.domain

import id.usecase.core.domain.assessment.utils.DataError
import id.usecase.core.domain.assessment.utils.EmptyResult
import id.usecase.core.domain.assessment.utils.Result
import id.usecase.evaluasi.authentication.domain.model.Login
import id.usecase.evaluasi.authentication.domain.model.Register
import id.usecase.evaluasi.authentication.domain.model.Teacher

interface AuthRepository {
    suspend fun login(request: Login): Result<Teacher, DataError.Network>
    suspend fun register(request: Register): Result<Teacher, DataError.Network>
    suspend fun logout(): EmptyResult<DataError.Network>
}