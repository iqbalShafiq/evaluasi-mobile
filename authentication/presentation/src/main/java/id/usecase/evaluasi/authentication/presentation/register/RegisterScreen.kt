package id.usecase.evaluasi.authentication.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.usecase.core.presentation.ui.ObserveAsEvents
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.dialog.EvaluasiAlertDialog
import id.usecase.designsystem.components.text_field.EvaluasiTextField
import id.usecase.evaluasi.authentication.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var errorMessage by remember {
        mutableStateOf("")
    }
    var showErrorDialog by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(
        flow = viewModel.event
    ) { event ->
        when (event) {
            is RegisterEvent.OnErrorOccurred -> {
                keyboardController?.hide()
                errorMessage = event.message
                showErrorDialog = true
            }

            RegisterEvent.OnRegisterSuccess -> {
                keyboardController?.hide()
                onRegisterSuccess()
            }
        }
    }

    // Show error dialog
    EvaluasiAlertDialog(
        showDialog = showErrorDialog,
        title = "Error Has Occurred",
        message = errorMessage,
        onConfirmation = {
            showErrorDialog = false
            errorMessage = ""
        }
    )

    RegisterScreen(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onBackPressed = onBackPressed
    )

}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordConfirmationVisible by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Create an account to continue",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Email Field
            EvaluasiTextField(
                value = state.email,
                onValueChange = {
                    onAction(
                        RegisterAction.OnFormUpdated(
                            state.copy(email = it)
                        )
                    )
                },
                label = "Email",
                placeholder = "Enter your email",
                errorMessage = state.errorEmail,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon"
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name Field
            EvaluasiTextField(
                value = state.name,
                onValueChange = {
                    onAction(
                        RegisterAction.OnFormUpdated(
                            state.copy(name = it)
                        )
                    )
                },
                label = "Name",
                placeholder = "Enter your name",
                errorMessage = state.errorName,
                imeAction = ImeAction.Next,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person Icon"
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            EvaluasiTextField(
                value = state.password,
                onValueChange = {
                    onAction(
                        RegisterAction.OnFormUpdated(
                            state.copy(password = it)
                        )
                    )
                },
                label = "Password",
                placeholder = "Enter your password",
                errorMessage = state.errorPassword,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                ImageVector.vectorResource(id = R.drawable.ic_visibility_off)
                            } else {
                                ImageVector.vectorResource(id = R.drawable.ic_visibility)
                            },
                            contentDescription = if (passwordVisible) {
                                "Hide Password"
                            } else {
                                "Show Password"
                            }
                        )
                    }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Confirmation Field
            EvaluasiTextField(
                value = state.passwordConfirmation,
                onValueChange = {
                    onAction(
                        RegisterAction.OnFormUpdated(
                            state.copy(passwordConfirmation = it)
                        )
                    )
                },
                label = "Password Confirmation",
                placeholder = "Enter your password confirmation",
                errorMessage = state.errorPasswordConfirmation,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordConfirmationVisible = !passwordConfirmationVisible }
                    ) {
                        Icon(
                            imageVector = if (passwordConfirmationVisible) {
                                ImageVector.vectorResource(id = R.drawable.ic_visibility_off)
                            } else {
                                ImageVector.vectorResource(id = R.drawable.ic_visibility)
                            },
                            contentDescription = if (passwordConfirmationVisible) {
                                "Hide Password"
                            } else {
                                "Show Password"
                            }
                        )
                    }
                },
                visualTransformation = if (passwordConfirmationVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            EvaluasiButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Register",
                onClick = { onAction(RegisterAction.Register) },
                enabled = !state.isRegistering,
                buttonType = ButtonType.PRIMARY
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Register Button
            EvaluasiButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Already have an account? Login",
                onClick = onBackPressed,
                enabled = !state.isRegistering,
                buttonType = ButtonType.SURFACE
            )
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    EvaluasiTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
            onBackPressed = {}
        )
    }
}