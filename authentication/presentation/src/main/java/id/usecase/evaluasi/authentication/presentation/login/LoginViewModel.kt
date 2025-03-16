package id.usecase.evaluasi.authentication.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.core.domain.utils.Result
import id.usecase.evaluasi.authentication.domain.AuthRepository
import id.usecase.evaluasi.authentication.domain.UserDataValidator
import id.usecase.evaluasi.authentication.domain.model.Login
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository,
    private val userDataValidator: UserDataValidator,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnFormUpdated -> {
                _state.update { action.state }

                val isValidEmail = userDataValidator.isValidEmail(state.value.email.text)
                if (state.value.email.text.isNotEmpty() && !isValidEmail) {
                    _state.update {
                        it.copy(
                            errorEmail = "Invalid email format",
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            errorEmail = null
                        )
                    }
                }
            }

            LoginAction.Login -> {
                viewModelScope.launch(dispatcher) {
                    _state.update { it.copy(isLoading = true) }

                    val request = Login(
                        email = state.value.email.text,
                        password = state.value.password.text
                    )
                    val result = repository.login(request)
                    _state.update { it.copy(isLoading = false) }

                    when (result) {
                        is Result.Error -> {
                            val errorMessage = result.error.message ?: "An error occurred"
                            _event.send(LoginEvent.OnErrorOccurred(errorMessage))
                        }

                        is Result.Success -> {
                            _event.send(LoginEvent.OnLoginSuccess)
                        }
                    }
                }
            }
        }
    }
}