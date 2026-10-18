package com.panalbit.panalsuite.data.repository
import com.panalbit.panalsuite.model.Rol
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

    // Simula la API. Después se reemplaza por la llamada a Retrofit.
    suspend fun getInbox(rol: Rol): List<Ticket> {
        delay(600)
        return when (rol) {
            Rol.SOLICITANTE -> tickets.take(3)
            Rol.RESOLUTOR -> tickets.filter { it.responsable == "Raúl Resolutor" }
            else -> tickets
        }
    }
}