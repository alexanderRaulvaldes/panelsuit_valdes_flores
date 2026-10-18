package com.panalbit.panalsuite.model

data class Ticket(
    val idRequest: String,
    val flowType: String,
    val state: String,
    val assignee: String?,
    val referenceLocation: String,
    val comments: List<String> = emptyList()
)