package com.herald.currencyapp.data.remote

import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.models.CurrencyDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {
    @GET("latest")
    suspend fun getAllCurrencies(@Query("access_key") accessKey: String): AllCurrencies
    suspend fun getCurrencyDetails(
        @Query("access_key") accessKey: String,
        @Query("base") baseCurrency: String
    ): CurrencyDetails
}