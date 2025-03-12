package id.usecase.evaluasi.authentication.presentation.register

import androidx.compose.ui.text.input.TextFieldValue

data class RegisterState(
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val email: TextFieldValue = TextFieldValue(""),
    val errorEmail: String? = null,
    val name: TextFieldValue = TextFieldValue(""),
    val errorName: String? = null,
    val password: TextFieldValue = TextFieldValue(""),
    val errorPassword: String? = null,
    val passwordConfirmation: TextFieldValue = TextFieldValue(""),
    val errorPasswordConfirmation: String? = null,
)
