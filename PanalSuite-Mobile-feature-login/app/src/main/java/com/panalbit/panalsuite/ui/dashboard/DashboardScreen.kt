package com.panalbit.panalsuite.ui.dashboard

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panalbit.panalsuite.model.*
import com.panalbit.panalsuite.ui.components.AppHeader
import com.panalbit.panalsuite.ui.components.PriorityChip
import com.panalbit.panalsuite.ui.components.StateChip
import com.panalbit.panalsuite.ui.theme.SemanticError
import com.panalbit.panalsuite.ui.theme.SemanticInfo
import com.panalbit.panalsuite.ui.theme.SemanticSuccess
import com.panalbit.panalsuite.ui.theme.SemanticWarning

@Composable
fun DashboardScreen(
    user: User,
    onLogout: () -> Unit,
    onTicketClick: (String) -> Unit,
    onCreateTicket: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(user.id) {
        vm.loadTickets(user)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppHeader(
                title = "Hola, ${user.name}",
                subtitle = "Rol: ${user.role.label()}",
                actionLabel = "Salir",
                onAction = onLogout
            )

            when {
                state.loading -> Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                state.error != null -> Text(
                    state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )

                else -> {
                    val visible = state.visibleTickets
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (user.role.canSeeStats()) {
                            item { StatsSection(state.tickets) }
                        }

                        item {
                            Column {
                                OutlinedTextField(
                                    value = state.query,
                                    onValueChange = { vm.onQueryChange(it) },
                                    label = { Text("Buscar por ID, tipo o ubicación") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = state.stateFilter == null,
                                        onClick = { vm.onFilterChange(null) },
                                        label = { Text("Todos") }
                                    )
                                    TicketState.all.forEach { option ->
                                        FilterChip(
                                            selected = state.stateFilter == option,
                                            onClick = { vm.onFilterChange(option) },
                                            label = { Text(option) }
                                        )
                                    }
                                }
                            }
                        }

                        if (visible.isEmpty()) {
                            item { Text("No hay tickets para mostrar") }
                        }

                        items(visible, key = { it.idRequest }) { ticket ->
                            TicketItem(ticket, onClick = { onTicketClick(ticket.idRequest) })
                        }
                    }
                }
            }
        }

        if (user.role.canCreateTicket()) {
            ExtendedFloatingActionButton(
                onClick = onCreateTicket,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Text("+ Nueva solicitud", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun StatsSection(tickets: List<Ticket>) {
    val total = tickets.size
    val open = tickets.count { it.state == TicketState.OPEN }
    val inProgress = tickets.count { it.state == TicketState.IN_PROGRESS || it.state == TicketState.PENDING }
    val closed = tickets.count { it.state == TicketState.CLOSED }
    val unassigned = tickets.count { it.assignee == null }
    val highPriority = tickets.count { it.priority == TicketPriority.HIGH && it.state != TicketState.CLOSED }

    // Active tickets per assignee (workload)
    val workload = tickets
        .filter { it.assignee != null && it.state != TicketState.CLOSED }
        .groupingBy { it.assignee!! }
        .eachCount()

    Column {
        Text("Resumen", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("Total", total, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            StatCard("Abiertos", open, SemanticInfo, Modifier.weight(1f))
            StatCard("En curso", inProgress, SemanticWarning, Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("Cerrados", closed, SemanticSuccess, Modifier.weight(1f))
            StatCard("Sin asignar", unassigned, SemanticError, Modifier.weight(1f))
            StatCard("Prioridad alta", highPriority, SemanticError, Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        Text("Carga por responsable", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        if (workload.isEmpty()) {
            Text(
                "Sin tickets activos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            workload.forEach { (name, count) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(name, style = MaterialTheme.typography.bodyMedium)
                    Text("$count activos", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                value.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
            PriorityChip(ticket.priority)
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
            Text(
                "Actualizado: ${ticket.updatedAt}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}