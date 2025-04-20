package ui


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ui.Element.FeedbackMessage
import ui.Element.OperationState
import ui.Element.*

@Composable
fun LoginScreen(onLoginRequested: (String, String, (OperationState<Unit>) -> Unit) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginState by remember { mutableStateOf<OperationState<Unit>>(OperationState.Initial) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Connexion",
            style = MaterialTheme.typography.h4,
            color = textPrimaryColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    loginState = OperationState.Loading
                    onLoginRequested(email, password) { newState ->
                        loginState = newState
                    }
                } else {
                    loginState = OperationState.Error("Email et mot de passe requis")
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor)
        ) {
            Text("Se connecter", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = loginState) {
            is OperationState.Error -> {
                FeedbackMessage(message = state.message, isError = true)
            }
            is OperationState.Loading -> {
                CircularProgressIndicator()
            }
            else -> { /* Ne rien afficher pour les autres états */ }
        }
    }
}
