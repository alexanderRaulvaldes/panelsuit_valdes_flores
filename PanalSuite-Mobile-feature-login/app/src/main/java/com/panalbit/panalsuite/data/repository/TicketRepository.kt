package com.panalbit.panalsuite.data.repository

import com.panalbit.panalsuite.model.Role
import com.panalbit.panalsuite.model.Ticket
import kotlinx.coroutines.delay

class TicketRepository {

    private val tickets = listOf(
        Ticket("TCK-001", "Mesa de ayuda", "Abierto", "Raúl Resolutor", "Sucursal Alameda"),
        Ticket("TCK-002", "Falla de infraestructura", "En proceso", "Raúl Resolutor", "Farmacia Centro"),
        Ticket("TCK-003", "Soporte interno", "Pendiente", null, "Oficina Central"),
        Ticket("TCK-004", "Gestión de gastos", "Cerrado", "Otro Resolutor", "Tienda Maipú"),
        Ticket("TCK-005", "Cobranzas", "En proceso", "Otro Resolutor", "Proveedor Norte")
    )

    // Simulates the API. Later it will be replaced by a Retrofit call.
    suspend fun getInbox(role: Role): List<Ticket> {
        delay(600)
        return when (role) {
            Role.REQUESTER -> tickets.take(3)
            Role.RESOLVER -> tickets.filter { it.assignee == "Raúl Resolutor" }
            else -> tickets
        }
    }
}