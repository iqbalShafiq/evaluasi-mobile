package id.usecase.evaluasi.authentication.presentation.login

sealed class LoginAction {
    data class OnFormUpdated(val state: LoginState) : LoginAction()
    data object Login : LoginAction()
}