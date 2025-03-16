package id.usecase.evaluasi.authentication.presentation.login

import androidx.compose.ui.text.input.TextFieldValue

data class LoginState(
    val isLoading: Boolean = false,
    val email: TextFieldValue = TextFieldValue(""),
    val errorEmail: String? = null,
    val password: TextFieldValue = TextFieldValue("")
)
