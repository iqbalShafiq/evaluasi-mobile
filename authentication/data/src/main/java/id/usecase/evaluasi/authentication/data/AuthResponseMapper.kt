package id.usecase.evaluasi.authentication.data

import id.usecase.evaluasi.authentication.domain.model.Teacher

fun AuthResponse.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)