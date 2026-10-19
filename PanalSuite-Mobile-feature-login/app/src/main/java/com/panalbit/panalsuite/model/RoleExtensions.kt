package com.panalbit.panalsuite.model
enum class Role {
    REQUESTER,
    RESOLVER,
    SUPERVISOR,
    MANAGER,
    ADMIN
}

fun Role.label(): String = when (this) {
    Role.REQUESTER -> "Solicitante"
    Role.RESOLVER -> "Resolutor"
    Role.SUPERVISOR -> "Supervisor"
    Role.MANAGER -> "Gerencia"
    Role.ADMIN -> "Administrador"
}

fun Role.canCreateTicket() = this == Role.REQUESTER || this == Role.ADMIN
fun Role.canChangeState() = this == Role.RESOLVER || this == Role.ADMIN
fun Role.canTakeTicket() = this == Role.RESOLVER
fun Role.canAssign() = this == Role.SUPERVISOR || this == Role.ADMIN
fun Role.canComment() = this != Role.MANAGER
fun Role.canSeeStats() = this == Role.MANAGER || this == Role.SUPERVISOR || this == Role.ADMIN