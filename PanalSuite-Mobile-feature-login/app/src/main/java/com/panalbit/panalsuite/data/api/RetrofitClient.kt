package com.panalbit.panalsuite.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val DB_URL="https://api-mock-panalsuite.com/"

    val apiServie: PanalSuiteApi by lazy {
        Retrofit.Builder()
            .baseUrl(DB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PanalSuiteApi::class.java)
    }
}