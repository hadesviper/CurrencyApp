package com.herald.currencyapp.domain.repository

import com.herald.currencyapp.common.Constants
import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.models.CurrencyExchange


interface RetroRepo {
    suspend fun getAllCurrencies(accessKey: String = Constants.Api_Key): AllCurrencies
    suspend fun getExchangeRate(
        accessKey: String = Constants.Api_Key,
        date: String,
        from: String, to: String
    ): CurrencyExchange

}