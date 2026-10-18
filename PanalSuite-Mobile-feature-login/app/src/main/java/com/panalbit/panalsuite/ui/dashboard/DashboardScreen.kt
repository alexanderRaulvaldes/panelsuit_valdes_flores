package com.panalbit.panalsuite.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panalbit.panalsuite.model.Ticket
import com.panalbit.panalsuite.model.User
import com.panalbit.panalsuite.ui.theme.SemanticInfo
import com.panalbit.panalsuite.ui.theme.SemanticSuccess
import com.panalbit.panalsuite.ui.theme.SemanticWarning

@Composable
fun DashboardScreen(
    user: User,
    onLogout: () -> Unit,
    onTicketClick: (String) -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(user.role) {
        vm.loadTickets(user.role)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Header with logout button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Hola, ${user.name}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "Rol: ${user.role}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
                TextButton(onClick = onLogout) {
                    Text("Salir", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        // Content
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                "Mi bandeja",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))

            when {
                state.loading -> CircularProgressIndicator()
                state.error != null -> Text(
                    state.error!!,
                    color = MaterialTheme.colorScheme.error
                )
                state.tickets.isEmpty() -> Text("No hay tickets para mostrar")
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.tickets) { ticket ->
                        TicketItem(ticket, onClick = { onTicketClick(ticket.idRequest) })
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItem(ticket: Ticket, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    ticket.idRequest,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StateChip(ticket.state)
            }
            Spacer(Modifier.height(4.dp))
            Text(ticket.flowType, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "Responsable: ${ticket.assignee ?: "Sin asignar"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Ubicación: ${ticket.referenceLocation}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StateChip(ticketState: String) {
    val color: Color = when (ticketState) {
        "Abierto" -> SemanticInfo
        "En proceso", "Pendiente" -> SemanticWarning
        "Cerrado" -> SemanticSuccess
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            ticketState,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}