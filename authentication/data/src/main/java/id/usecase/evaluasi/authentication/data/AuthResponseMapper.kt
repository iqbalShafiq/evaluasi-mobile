package id.usecase.evaluasi.authentication.data

import id.usecase.evaluasi.authentication.data.model.AuthResponse
import id.usecase.evaluasi.authentication.data.model.RegisterResponse
import id.usecase.evaluasi.authentication.domain.model.Teacher

fun AuthResponse.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)

fun RegisterResponse.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)