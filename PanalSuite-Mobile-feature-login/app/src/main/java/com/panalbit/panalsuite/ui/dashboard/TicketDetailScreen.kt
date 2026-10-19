package com.panalbit.panalsuite.ui.dashboard

import androidx.activity.compose.BackHandler
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
import com.panalbit.panalsuite.model.*
import com.panalbit.panalsuite.ui.components.AppHeader
import com.panalbit.panalsuite.ui.components.PriorityChip
import com.panalbit.panalsuite.ui.components.StateChip

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

    // The phone's back button also returns to the inbox
    BackHandler { onBack() }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(title = ticket?.idRequest ?: "Ticket", onBack = onBack)

        if (ticket == null) {
            Text("Ticket no encontrado", modifier = Modifier.padding(16.dp))
            return@Column
        }

        // Permissions for this user on this ticket
        val isAssignee = ticket.assignee == user.name
        val canChangeState = user.role.canChangeState() && (user.role == Role.ADMIN || isAssignee)
        val canTake = user.role.canTakeTicket() && ticket.assignee == null

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ticket information
            item {
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
                            Text(
                                ticket.flowType,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            StateChip(ticket.state)
                        }
                        Spacer(Modifier.height(8.dp))
                        PriorityChip(ticket.priority)
                        Spacer(Modifier.height(8.dp))
                        Text(ticket.description, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        DetailLine("Solicitante", ticket.requester)
                        DetailLine("Responsable", ticket.assignee ?: "Sin asignar")
                        DetailLine("Ubicación", ticket.referenceLocation)
                        DetailLine("Creado", ticket.createdAt)
                        DetailLine("Actualizado", ticket.updatedAt)
                    }
                }
            }

            // Assignment
            if (canTake || user.role.canAssign()) {
                item {
                    Column {
                        Text(
                            "Asignación",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(8.dp))
                        if (canTake) {
                            Button(
                                onClick = { vm.assign(ticket.idRequest, user.name, user.name) },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Tomar ticket")
                            }
                        }
                        if (user.role.canAssign()) {
                            Text("Asignar a:", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                vm.resolvers.forEach { name ->
                                    FilterChip(
                                        selected = ticket.assignee == name,
                                        onClick = { vm.assign(ticket.idRequest, name, user.name) },
                                        label = { Text(name) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Change state osea cambaistmos el estado
            item {
                Column {
                    Text(
                        "Cambiar estado",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    if (canChangeState) {
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TicketState.all.forEach { option ->
                                FilterChip(
                                    selected = ticket.state == option,
                                    onClick = { vm.changeState(ticket.idRequest, option, user.name) },
                                    label = { Text(option) }
                                )
                            }
                        }
                    } else {
                        val message = if (user.role.canChangeState()) {
                            "Toma el ticket para poder cambiar su estado."
                        } else {
                            "Tu rol solo puede consultar el estado."
                        }
                        Text(
                            message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Comments
            item {
                Text(
                    "Comentarios (${ticket.comments.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (ticket.comments.isEmpty()) {
                item {
                    Text(
                        "Aún no hay comentarios.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(ticket.comments) { c ->
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            c.author,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(c.text, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            c.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // History solo para admin)
            item {
                Column {
                    Text(
                        "Historial de cambios",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    ticket.history.reversed().forEach { event ->
                        Text(
                            "${event.date} · ${event.author}: ${event.description}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // aca solo se muestra el comentario y lo puede cambiar el usuario
        if (user.role.canComment()) {
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
                        vm.addComment(ticket.idRequest, comment, user.name)
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

@Composable
private fun DetailLine(label: String, value: String) {
    Text(
        "$label: $value",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
