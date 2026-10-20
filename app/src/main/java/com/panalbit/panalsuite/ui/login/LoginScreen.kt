package com.panalbit.panalsuite.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panalbit.panalsuite.model.User
import com.panalbit.panalsuite.ui.theme.AccentAction
import com.panalbit.panalsuite.ui.theme.CorporatePrimary
import com.panalbit.panalsuite.ui.theme.CorporatePrimaryVariant

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    vm: LoginViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(state.user) {
        state.user?.let {
            onLoginSuccess(it)
            vm.clearUser()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(CorporatePrimary, CorporatePrimaryVariant)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Box(
                modifier = Modifier.size(80.dp).background(AccentAction, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("P", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            Text("PanalSuite", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text(
                "Gestión móvil de flujos de trabajo",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))

            // Form card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        "Iniciar sesión",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { vm.onEmailChange(it) },
                        label = { Text("Correo") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { vm.onPasswordChange(it) },
                        label = { Text("Contraseña") },
                        trailingIcon = {
                            TextButton(onClick = { showPassword = !showPassword }) {
                                Text(if (showPassword) "Ocultar" else "Mostrar")
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (showPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    state.error?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { vm.login() },
                        enabled = !state.loading,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        if (state.loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Ingresar", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Quick test access
            Text(
                "Acceso rápido (datos de prueba)",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickAccessChip("Solicitante", "solicitante@panalbit.cl", vm)
                QuickAccessChip("Resolutor", "resolutor@panalbit.cl", vm)
                QuickAccessChip("Admin", "admin@panalbit.cl", vm)
            }
        }
    }
}

@Composable
private fun QuickAccessChip(label: String, email: String, vm: LoginViewModel) {
    AssistChip(
        onClick = {
            vm.onEmailChange(email)
            vm.onPasswordChange("1234")
        },
        label = { Text(label, color = Color.White) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color.White.copy(alpha = 0.12f)
        ),
        border = null
    )
}