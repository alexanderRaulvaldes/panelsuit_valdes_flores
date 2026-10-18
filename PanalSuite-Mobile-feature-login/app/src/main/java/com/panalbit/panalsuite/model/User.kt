package com.panalbit.panalsuite.model

enum class Role { REQUESTER, RESOLVER, SUPERVISOR, MANAGER, ADMIN }

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: Role
)