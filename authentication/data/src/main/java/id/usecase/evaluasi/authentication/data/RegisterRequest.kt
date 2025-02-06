package id.usecase.evaluasi.authentication.data

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String
)
