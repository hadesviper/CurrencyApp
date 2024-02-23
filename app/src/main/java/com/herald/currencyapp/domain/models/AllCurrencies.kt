package com.herald.currencyapp.domain.models

data class AllCurrencies(
    val success: Boolean,
    val symbols: Map<String, String>,
    val error: ApiError = ApiError()
){
    data class ApiError(
        val code: Int = 0,
        val type: String = "",
        val info: String = ""
    )
}
