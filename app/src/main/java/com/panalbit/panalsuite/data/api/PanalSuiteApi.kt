package com.panalbit.panalsuite.data.api

import com.panalbit.panalsuite.model.Ticket
import com.panalbit.panalsuite.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
   // DTOS
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

interface PanalSuiteApi {

    // Autenticación
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>

    // Flujo de Tickets
    @GET("api/tickets")
    suspend fun getTickets(): Response<List<Ticket>>

    @POST("api/tickets")
    suspend fun createTicket(@Body ticket: Ticket): Response<Ticket>

    @PUT("api/tickets/{id}")
    suspend fun updateTicket(
        @Path("id") id: String,
        @Body ticket: Ticket
    ): Response<Ticket>
}