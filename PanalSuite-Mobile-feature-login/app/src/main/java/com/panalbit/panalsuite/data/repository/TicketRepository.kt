package com.panalbit.panalsuite.data.repository

import com.panalbit.panalsuite.model.*
import com.panalbit.panalsuite.util.DateUtils
import kotlinx.coroutines.delay

class TicketRepository {

    val resolvers = listOf("Raúl Resolutor", "Otro Resolutor")

    private val tickets = mutableListOf(
        seed("TCK-001", "Mesa de ayuda", TicketPriority.MEDIUM, TicketState.OPEN,
            "Ana Solicitante", "Raúl Resolutor", "Sucursal Alameda",
            "La caja no enciende al iniciar el turno."),
        seed("TCK-002", "Falla de infraestructura", TicketPriority.HIGH, TicketState.IN_PROGRESS,
            "Ana Solicitante", "Raúl Resolutor", "Farmacia Centro",
            "La tienda no tiene conexión a internet."),
        seed("TCK-003", "Soporte interno", TicketPriority.LOW, TicketState.PENDING,
            "Ana Solicitante", null, "Oficina Central",
            "Solicitud de acceso a la carpeta compartida."),
        seed("TCK-004", "Gestión de gastos", TicketPriority.MEDIUM, TicketState.CLOSED,
            "Otro Solicitante", "Otro Resolutor", "Tienda Maipú",
            "Rendición de gastos con error de monto."),
        seed("TCK-005", "Cobranzas", TicketPriority.HIGH, TicketState.IN_PROGRESS,
            "Otro Solicitante", "Otro Resolutor", "Proveedor Norte",
            "Factura vencida sin registrar.")
    )

    // Esto esta simulando la appi asi depues lo cambias por el retrofill nomas
    suspend fun getInbox(user: User): List<Ticket> {
        delay(600)
        return when (user.role) {
            Role.REQUESTER -> tickets.filter { it.requester == user.name }
            Role.RESOLVER -> tickets.filter { it.assignee == user.name || it.assignee == null }
            else -> tickets.toList()
        }
    }

    fun create(
        user: User,
        flowType: String,
        priority: String,
        location: String,
        description: String
    ): Ticket {
        val now = DateUtils.now()
        val ticket = Ticket(
            idRequest = nextId(),
            flowType = flowType,
            priority = priority,
            state = TicketState.OPEN,
            requester = user.name,
            assignee = null,
            referenceLocation = location,
            description = description,
            createdAt = now,
            updatedAt = now,
            history = listOf(TicketEvent(user.name, "Ticket creado", now))
        )
        tickets.add(0, ticket)
        return ticket
    }

    fun update(ticket: Ticket) {
        val index = tickets.indexOfFirst { it.idRequest == ticket.idRequest }
        if (index >= 0) tickets[index] = ticket
    }

    private fun nextId(): String {
        val max = tickets.maxOfOrNull { it.idRequest.removePrefix("TCK-").toIntOrNull() ?: 0 } ?: 0
        return "TCK-%03d".format(max + 1)
    }

    private fun seed(
        id: String, flow: String, priority: String, state: String,
        requester: String, assignee: String?, location: String, description: String
    ): Ticket {
        val date = "13-10-2026 09:00"
        return Ticket(
            id, flow, priority, state, requester, assignee, location, description,
            date, date,
            history = listOf(TicketEvent(requester, "Ticket creado", date))
        )
    }
}