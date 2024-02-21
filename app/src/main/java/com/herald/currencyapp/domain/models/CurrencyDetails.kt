package com.herald.currencyapp.domain.models

import com.google.gson.annotations.SerializedName


data class CurrencyDetails(
    val base: String = "",
    val date: String = "",
    @SerializedName("rates")
    val rates: Rates = Rates(),
) {
    data class Rates(
        val AUD: Double = 0.0,
        val CAD: Double = 0.0,
        val CHF: Double = 0.0,
        val RUB: Double = 0.0,
        val GBP: Double = 0.0,
        val SEK: Double = 0.0,
        val NZD: Double = 0.0,
        val CNY: Double = 0.0,
        val JPY: Double = 0.0,
        val USD: Double = 0.0
    )
}