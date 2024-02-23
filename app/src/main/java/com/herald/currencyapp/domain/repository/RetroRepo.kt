package com.herald.currencyapp.domain.repository

import com.herald.currencyapp.common.Constants
import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.models.CurrencyExchange
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


interface RetroRepo {
    suspend fun getAllCurrencies(accessKey: String = Constants.Api_Key): AllCurrencies
    suspend fun getExchangeRate(
        accessKey: String = Constants.Api_Key,
        date: String = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).format(Calendar.getInstance().time),
        from: String, to: String
    ): CurrencyExchange

}