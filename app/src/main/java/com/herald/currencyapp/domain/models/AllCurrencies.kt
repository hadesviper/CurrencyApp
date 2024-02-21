package com.herald.currencyapp.domain.models

data class AllCurrencies(
    val success: Boolean,
    val symbols: Map<String, String>
)
