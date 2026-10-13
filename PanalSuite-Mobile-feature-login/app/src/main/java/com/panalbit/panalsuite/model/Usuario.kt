package com.panalbit.panalsuite.model

enum class Rol { SOLICITANTE, RESOLUTOR, SUPERVISOR, GERENCIA, ADMINISTRADOR }

data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String,
    val rol: Rol
)