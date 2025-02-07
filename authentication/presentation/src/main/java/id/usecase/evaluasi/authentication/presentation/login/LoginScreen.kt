package id.usecase.evaluasi.authentication.presentation.login

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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.usecase.designsystem.EvaluasiTheme
import id.usecase.designsystem.components.button.ButtonType
import id.usecase.designsystem.components.button.EvaluasiButton
import id.usecase.designsystem.components.text_field.EvaluasiTextField
import id.usecase.evaluasi.authentication.presentation.R

@Composable
fun LoginScreenRoot(modifier: Modifier = Modifier) {

}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onRegisterButtonClicked: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email Field
            EvaluasiTextField(
                value = state.email,
                onValueChange = {
                    onAction(
                        LoginAction.OnFormUpdated(
                            state.copy(email = it)
                        )
                    )
                },
                label = "Email",
                placeholder = "Enter your email",
                errorMessage = state.email.annotatedString.text,
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

            // Password Field
            EvaluasiTextField(
                value = state.password,
                onValueChange = {
                    onAction(
                        LoginAction.OnFormUpdated(
                            state.copy(password = it)
                        )
                    )
                },
                label = "Password",
                placeholder = "Enter your password",
                errorMessage = state.password.annotatedString.text,
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

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            EvaluasiButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Login",
                onClick = { onAction(LoginAction.Login) },
                enabled = !state.isLoading,
                buttonType = ButtonType.PRIMARY
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Register Button
            EvaluasiButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Create an Account",
                onClick = onRegisterButtonClicked,
                enabled = !state.isLoading,
                buttonType = ButtonType.SURFACE
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    EvaluasiTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            onRegisterButtonClicked = {}
        )
    }
}