package com.panalbit.panalsuite.model

data class Ticket(
    val idRequest: String,
    val flowType: String,
    val state:String,
    val responsable: String?,
    val referenceLocation: String
)
