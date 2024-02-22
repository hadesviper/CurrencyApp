package com.herald.currencyapp.data.remote.repository

import com.herald.currencyapp.common.Constants
import com.herald.currencyapp.data.remote.RetroService
import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.repository.RetroRepo
import javax.inject.Inject

class RetroImpl @Inject constructor(
    private val retroService: RetroService
) : RetroRepo {
    override suspend fun getAllCurrencies(accessKey: String): AllCurrencies {
        return retroService.getAllCurrencies(accessKey = Constants.Api_Key)
    }

    override suspend fun getExchangeRate(
        accessKey: String,
        date:String,
        from: String,
        to: String
    ): CurrencyExchange {
        return retroService.getExchangeRate(accessKey = accessKey, date = date, symbols = "$from,$to" )
    }
}