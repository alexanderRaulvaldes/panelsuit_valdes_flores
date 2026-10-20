package com.panalbit.panalsuite.model


object TicketState {
    const val OPEN = "Abierto"
    const val IN_PROGRESS = "En proceso"
    const val PENDING = "Pendiente"
    const val CLOSED = "Cerrado"
    val all = listOf(OPEN, IN_PROGRESS, PENDING, CLOSED)
}

object TicketPriority {
    const val HIGH = "Alta"
    const val MEDIUM = "Media"
    const val LOW = "Baja"
    val all = listOf(HIGH, MEDIUM, LOW)
}

object TicketFlow {
    val all = listOf(
        "Mesa de ayuda",
        "Falla de infraestructura",
        "Soporte interno",
        "Gestión de gastos",
        "Cobranzas"
    )
}