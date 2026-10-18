package com.panalbit.panalsuite.data.repository

import com.panalbit.panalsuite.model.Role
import com.panalbit.panalsuite.model.User
import kotlinx.coroutines.delay

class AuthRepository {

    private val users = listOf(
        User(1, "Ana Solicitante", "solicitante@panalbit.cl", Role.REQUESTER),
        User(2, "Raúl Resolutor", "resolutor@panalbit.cl", Role.RESOLVER),
        User(3, "Sofía Supervisora", "supervisor@panalbit.cl", Role.SUPERVISOR),
        User(4, "Admin Prueba", "admin@panalbit.cl", Role.ADMIN)
    )

    // returna si el usuario existe o no y si es valido o no
    suspend fun login(email: String, password: String): User? {
        delay(800)
        return users.find { it.email == email.trim().lowercase() && password == "1234" }
    }
}