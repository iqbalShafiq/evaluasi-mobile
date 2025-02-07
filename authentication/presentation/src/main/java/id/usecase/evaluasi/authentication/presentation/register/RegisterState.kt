package id.usecase.evaluasi.authentication.presentation.register

import androidx.compose.ui.text.input.TextFieldValue
import id.usecase.evaluasi.authentication.domain.PasswordValidationState

data class RegisterState(
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val email: TextFieldValue = TextFieldValue(""),
    val name: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue(""),
    val passwordConfirmation: TextFieldValue = TextFieldValue(""),
    val passwordValidation: PasswordValidationState = PasswordValidationState(),
)
