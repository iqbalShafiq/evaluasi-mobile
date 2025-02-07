package id.usecase.evaluasi.authentication.presentation.register

sealed class RegisterEvent {
    data class OnErrorOccurred(val message: String) : RegisterEvent()
    data object OnRegisterSuccess : RegisterEvent()
}