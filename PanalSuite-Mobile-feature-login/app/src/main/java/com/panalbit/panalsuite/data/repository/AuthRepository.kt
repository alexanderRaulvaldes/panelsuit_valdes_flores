package com.panalbit.panalsuite.data.repository

import com.panalbit.panalsuite.model.Rol
import com.panalbit.panalsuite.model.Usuario
import kotlinx.coroutines.delay

class AuthRepository {

    private val usuarios = listOf(
        Usuario(1, "Ana Solicitante", "solicitante@panalbit.cl", Rol.SOLICITANTE),
        Usuario(2, "Raúl Resolutor", "resolutor@panalbit.cl", Rol.RESOLUTOR),
        Usuario(3, "Sofía Supervisora", "supervisor@panalbit.cl", Rol.SUPERVISOR),
        Usuario(4, "Admin Prueba", "admin@panalbit.cl", Rol.ADMINISTRADOR)
    )


    suspend fun login(email: String, password: String): Usuario? {
        delay(800)
        return usuarios.find { it.email == email.trim().lowercase() && password == "1234" }
    }
}