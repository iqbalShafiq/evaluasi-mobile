package id.usecase.evaluasi.authentication.presentation.register

sealed class RegisterAction {
    data class OnFormUpdated(val state: RegisterState) : RegisterAction()
    data object Register : RegisterAction()
}