package id.usecase.evaluasi.authentication.presentation.register

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.usecase.core.domain.utils.Result
import id.usecase.evaluasi.authentication.domain.AuthRepository
import id.usecase.evaluasi.authentication.domain.UserDataValidator
import id.usecase.evaluasi.authentication.domain.model.Register
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository,
    private val userDataValidator: UserDataValidator,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _event = Channel<RegisterEvent>()
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnFormUpdated -> {
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

                val isValidName = state.value.name.text.isNotEmpty()
                if (state.value.name.text.isNotEmpty() && !isValidName) {
                    _state.update {
                        it.copy(
                            errorName = "Name must not be empty"
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            errorName = null
                        )
                    }
                }

                val passwordValidationState = userDataValidator.validatePassword(
                    state.value.password.text
                )
                val isValidPassword = passwordValidationState.isValid
                if (state.value.password.text.isNotEmpty() && !isValidPassword) {
                    _state.update {
                        it.copy(
                            errorPassword = "Invalid password"
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            errorPassword = null
                        )
                    }
                }

                val isValidPasswordConfirmation = state.value.password.text ==
                        state.value.passwordConfirmation.text
                if (state.value.passwordConfirmation.text.isNotEmpty() && !isValidPasswordConfirmation) {
                    _state.update {
                        it.copy(
                            errorPasswordConfirmation = "Password confirmation must match",
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            errorPasswordConfirmation = null
                        )
                    }
                }

                _state.update {
                    it.copy(
                        canRegister = isValidEmail &&
                                isValidName &&
                                isValidPassword &&
                                isValidPasswordConfirmation
                    )
                }
            }

            RegisterAction.Register -> {
                viewModelScope.launch(dispatcher) {
                    _state.update { it.copy(isRegistering = true) }

                    val request = Register(
                        email = state.value.email.text,
                        name = state.value.name.text,
                        password = state.value.password.text
                    )

                    _state.update { it.copy(isRegistering = false) }

                    when (val result = repository.register(request)) {
                        is Result.Error -> {
                            val errorMessage = result.error.message ?: "An error occurred"
                            _event.send(RegisterEvent.OnErrorOccurred(errorMessage))
                        }

                        is Result.Success -> _event.send(RegisterEvent.OnRegisterSuccess)
                    }
                }
            }
        }
    }
}