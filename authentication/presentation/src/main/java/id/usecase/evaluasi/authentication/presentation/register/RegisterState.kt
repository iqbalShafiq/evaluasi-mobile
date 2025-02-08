package id.usecase.evaluasi.authentication.presentation.register

import androidx.compose.ui.text.input.TextFieldValue

data class RegisterState(
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val email: TextFieldValue = TextFieldValue(""),
    val name: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue(""),
    val passwordConfirmation: TextFieldValue = TextFieldValue("")
)
