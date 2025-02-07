package id.usecase.evaluasi.authentication.presentation.login

sealed class LoginEvent {
    data class OnErrorOccurred(val message: String) : LoginEvent()
    data object OnLoginSuccess : LoginEvent()
}