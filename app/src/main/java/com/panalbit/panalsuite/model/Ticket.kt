package com.panalbit.panalsuite.model

data class TicketComment(
    //lo que hace el tiket es que guarda quein comento lo qu ehizo y cuando
    val author: String,
    val text: String,
    val date: String
)

data class TicketEvent(
    // este es el historial de los tickets para saber quien lo hizo y cuando
    val author: String,
    val description: String,
    val date: String
)

data class Ticket(
    // max aqui se agregaron proridades como solicitante descripción y fechas que el caso lista como información necesaria
    val idRequest: String,
    val flowType: String,
    val priority: String,
    val state: String,
    val requester: String,
    val assignee: String?,
    val referenceLocation: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val comments: List<TicketComment> = emptyList(),
    val history: List<TicketEvent> = emptyList()
)