package com.herald.currencyapp.data.remote.repository

import com.herald.currencyapp.data.remote.RetroService
import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.models.CurrencyDetails
import com.herald.currencyapp.domain.repository.RetroRepo
import javax.inject.Inject

class RetroImpl @Inject constructor(
    private val retroService: RetroService
) : RetroRepo {
    override suspend fun getAllCurrencies(accessKey: String): AllCurrencies {
        return retroService.getAllCurrencies(accessKey)
    }

    override suspend fun getCurrencyDetails(
        accessKey: String,
        currencySymbol: String
    ): CurrencyDetails {
        return retroService.getCurrencyDetails(accessKey,currencySymbol)
    }

}