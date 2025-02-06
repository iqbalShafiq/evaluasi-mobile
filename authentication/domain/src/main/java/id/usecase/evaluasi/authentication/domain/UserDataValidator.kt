package id.usecase.evaluasi.authentication.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {
    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email)
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasNumber = password.any { it.isDigit() }
        val hasMinLength = password.length >= PASSWORD_MIN_LENGTH
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }

        return PasswordValidationState(
            hasNumber = hasNumber,
            hasMinLength = hasMinLength,
            hasSpecialChar = hasSpecialChar,
            hasUpperCase = hasUpperCase,
            hasLowerCase = hasLowerCase
        )
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 9
    }
}