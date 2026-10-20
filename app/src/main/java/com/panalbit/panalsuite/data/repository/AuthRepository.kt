package com.panalbit.panalsuite.data.repository

import com.panalbit.panalsuite.data.api.LoginRequest
import com.panalbit.panalsuite.data.api.RetrofitClient
import com.panalbit.panalsuite.model.Role
import com.panalbit.panalsuite.model.User
import kotlinx.coroutines.delay

class AuthRepository {

    private val api = RetrofitClient.api

    // Usuarios de respaldo en memoria
    private val fallbackUsers = listOf(
        User(1, "Ana Solicitante", "solicitante@panalbit.cl", Role.REQUESTER),
        User(2, "Raúl Resolutor", "resolutor@panalbit.cl", Role.RESOLVER),
        User(3, "Sofía Supervisora", "supervisor@panalbit.cl", Role.SUPERVISOR),
        User(4, "Admin Prueba", "admin@panalbit.cl", Role.ADMIN),
        User(5, "Gael Gerencia", "gerencia@panalbit.cl", Role.MANAGER)
    )

    suspend fun login(email: String, password: String): User? {
        return try {
            val response = api.login(LoginRequest(email.trim().lowercase(), password))
            if (response.isSuccessful) {
                response.body()?.user
            } else {
                fallbackLogin(email, password)
            }
        } catch (e: Exception) {
            fallbackLogin(email, password) //via secundaria
        }
    }

    suspend fun findById(id: Int): User? {
        return try {
            val response = api.getUserById(id)
            if (response.isSuccessful) response.body() else fallbackUsers.find { it.id == id }
        } catch (e: Exception) {
            fallbackUsers.find { it.id == id }
        }
    }

    private suspend fun fallbackLogin(email: String, password: String): User? {
        delay(600)
        return fallbackUsers.find { it.email == email.trim().lowercase() && password == "1234" }
    }
}