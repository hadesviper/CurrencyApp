package com.herald.currencyapp.data.remote

import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.models.CurrencyExchange
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetroService {
    @GET("latest")
    suspend fun getAllCurrencies(@Query("access_key") accessKey: String): AllCurrencies

    @GET("{date}")
    suspend fun getExchangeRate(
        @Path("date") date: String,
        @Query("access_key") accessKey: String,
        @Query("symbols") symbols: String
    ): CurrencyExchange
}