package com.herald.currencyapp.domain.models


data class CurrencyExchange(
    val success: Boolean = false,
    val base: String = "",
    val date: String = "",
    val rates: Map<String, Double>,
    val error: ApiError = ApiError()
){
    data class ApiError(
        val code: Int = 0,
        val type: String = "",
        val info: String = ""
    )
}
