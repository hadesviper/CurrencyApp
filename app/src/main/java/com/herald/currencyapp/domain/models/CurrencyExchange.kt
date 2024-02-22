package com.herald.currencyapp.domain.models

import com.google.gson.annotations.SerializedName


data class CurrencyExchange(
    val base: String = "",
    val date: String = "",
    @SerializedName("rates")
    val rates: Map<String, Double>,
)