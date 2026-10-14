package com.panalbit.panalsuite.data.api

import com.panalbit.panalsuite.model.Ticket
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PanalSuiteApi {
    @GET("api/v1/tickets")
    suspend fun getInbox(@Query("rol") rolUsuario: String):Response<List<Ticket>>
}