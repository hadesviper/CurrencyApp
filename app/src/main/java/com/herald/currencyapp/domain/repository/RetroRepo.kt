package com.herald.currencyapp.domain.repository

import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.models.CurrencyDetails


interface RetroRepo {
    suspend fun getAllCurrencies(accessKey: String): AllCurrencies
    suspend fun getCurrencyDetails(accessKey: String,currencySymbol: String): CurrencyDetails

}