package com.herald.currencyapp.domain.models


data class CurrencyExchange(
    val base: String = "",
    val date: String = "",
    val rates: Map<String, Double>,
)