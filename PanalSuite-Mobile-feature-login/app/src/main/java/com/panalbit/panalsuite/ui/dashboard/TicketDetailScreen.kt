package com.panalbit.panalsuite.ui.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panalbit.panalsuite.model.Role
import com.panalbit.panalsuite.model.User

private val STATES = listOf("Abierto", "En proceso", "Pendiente", "Cerrado")

@Composable
fun TicketDetailScreen(
    ticketId: String,
    user: User,
    onBack: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val ticket = state.tickets.find { it.idRequest == ticketId }
    var comment by remember { mutableStateOf("") }

    // Permissions by role
    val canChangeState = user.role == Role.RESOLVER || user.role == Role.ADMIN
    val canComment = user.role != Role.MANAGER

    // The phone's back button also returns to the inbox
    BackHandler { onBack() }

    Column(modifier = Modifier.fillMaxSize()) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) {
                    Text("← Volver", color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    ticket?.idRequest ?: "Ticket",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (ticket == null) {
            Text("Ticket no encontrado", modifier = Modifier.padding(16.dp))
            return@Column
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Ticket information
            Card(
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
                        Text(ticket.flowType, style = MaterialTheme.typography.titleMedium)
                        StateChip(ticket.state)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Responsable: ${ticket.assignee ?: "Sin asignar"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Ubicación: ${ticket.referenceLocation}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Change state (only Resolver and Admin)
            Spacer(Modifier.height(16.dp))
            if (canChangeState) {
                Text(
                    "Cambiar estado",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    STATES.forEach { option ->
                        FilterChip(
                            selected = ticket.state == option,
                            onClick = { vm.changeState(ticket.idRequest, option) },
                            label = { Text(option) }
                        )
                    }
                }
            } else {
                Text(
                    "Tu rol solo puede consultar el estado.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Comments
            Spacer(Modifier.height(16.dp))
            Text(
                "Comentarios (${ticket.comments.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            if (ticket.comments.isEmpty()) {
                Text(
                    "Aún no hay comentarios.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ticket.comments) { text ->
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }

        // Input box (only if the role can comment)
        if (canComment) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Escribe un comentario") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        vm.addComment(ticket.idRequest, "${user.name}: $comment")
                        comment = ""
                    },
                    enabled = comment.isNotBlank(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Enviar")
                }
            }
        }
    }
}
