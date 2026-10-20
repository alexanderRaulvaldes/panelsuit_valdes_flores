package com.panalbit.panalsuite.data.repository

import com.panalbit.panalsuite.data.api.RetrofitClient
import com.panalbit.panalsuite.model.*
import com.panalbit.panalsuite.util.DateUtils
import kotlinx.coroutines.delay

class TicketRepository {

    private val api = RetrofitClient.api
    val resolvers = listOf("Raúl Resolutor", "Otro Resolutor")

    //respaldo en memoria
    private val mockTickets = mutableListOf(
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

    suspend fun getInbox(user: User): List<Ticket> {
        return try {
            val response = api.getTickets()
            if (response.isSuccessful && response.body() != null) {
                filterForUser(response.body()!!, user)
            } else {
                getFallbackInbox(user)
            }
        } catch (e: Exception) {
            getFallbackInbox(user)
        }
    }

    suspend fun create(
        user: User,
        flowType: String,
        priority: String,
        location: String,
        description: String
    ): Ticket {
        val now = DateUtils.now()
        val newTicket = Ticket(
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

        return try {
            val response = api.createTicket(newTicket)
            if (response.isSuccessful && response.body() != null) {
                val created = response.body()!!
                mockTickets.add(0, created)
                created
            } else {
                fallbackCreate(newTicket)
            }
        } catch (e: Exception) {
            fallbackCreate(newTicket)
        }
    }

    suspend fun update(ticket: Ticket) {
        try {
            api.updateTicket(ticket.idRequest, ticket)
        } catch (_: Exception) {
        } finally {
            val index = mockTickets.indexOfFirst { it.idRequest == ticket.idRequest }
            if (index >= 0) mockTickets[index] = ticket
        }
    }

    private suspend fun getFallbackInbox(user: User): List<Ticket> {
        delay(400)
        return filterForUser(mockTickets, user)
    }

    private fun filterForUser(list: List<Ticket>, user: User): List<Ticket> {
        return when (user.role) {
            Role.REQUESTER -> list.filter { it.requester == user.name }
            Role.RESOLVER -> list.filter { it.assignee == user.name || it.assignee == null }
            else -> list.toList()
        }
    }

    private fun fallbackCreate(ticket: Ticket): Ticket {
        mockTickets.add(0, ticket)
        return ticket
    }

    private fun nextId(): String {
        val max = mockTickets.maxOfOrNull { it.idRequest.removePrefix("TCK-").toIntOrNull() ?: 0 } ?: 0
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