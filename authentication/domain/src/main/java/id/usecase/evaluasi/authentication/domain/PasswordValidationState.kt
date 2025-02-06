package id.usecase.evaluasi.authentication.domain

data class PasswordValidationState(
    val hasNumber: Boolean = false,
    val hasMinLength: Boolean = false,
    val hasSpecialChar: Boolean = false,
    val hasUpperCase: Boolean = false,
    val hasLowerCase: Boolean = false
) {
    val isValid: Boolean
        get() = hasNumber && hasMinLength && hasSpecialChar && hasUpperCase && hasLowerCase
}