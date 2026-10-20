package com.panalbit.panalsuite.ui.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panalbit.panalsuite.model.*
import com.panalbit.panalsuite.ui.components.AppHeader

@Composable
fun CreateTicketScreen(
    user: User,
    onBack: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    var flowType by remember { mutableStateOf(TicketFlow.all.first()) }
    var priority by remember { mutableStateOf(TicketPriority.MEDIUM) }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    BackHandler { onBack() }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(
            title = "Nueva solicitud",
            subtitle = "Solicitante: ${user.name}",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Tipo de flujo", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TicketFlow.all.forEach { option ->
                    FilterChip(
                        selected = flowType == option,
                        onClick = { flowType = option },
                        label = { Text(option) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Prioridad", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TicketPriority.all.forEach { option ->
                    FilterChip(
                        selected = priority == option,
                        onClick = { priority = option },
                        label = { Text(option) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Sucursal o ubicación") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción del problema") },
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    vm.createTicket(user, flowType, priority, location.trim(), description.trim())
                    onBack()
                },
                enabled = location.isNotBlank() && description.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Crear solicitud", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}