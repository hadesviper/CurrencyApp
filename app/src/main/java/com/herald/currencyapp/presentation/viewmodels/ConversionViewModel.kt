package com.herald.currencyapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.usecases.GetExchangeRateUseCase
import com.herald.currencyapp.domain.usecases.GetPopularCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val getPopularCurrenciesUseCase: GetPopularCurrenciesUseCase,
    private val getExchangeRateUseCase: GetExchangeRateUseCase
) : ViewModel() {

    private val _statePopularCurrencies = MutableLiveData<StateCurrency>()
    val statePopularCurrencies: LiveData<StateCurrency> = _statePopularCurrencies


    private val _stateExchange = MutableLiveData<StateCurrency>()
    val stateExchange: LiveData<StateCurrency> = _stateExchange


    fun getPopularCurrencies(currency: String) {
        getPopularCurrenciesUseCase(currency).onEach { it ->
            when (it) {
                is Resources.Loading -> {
                    _statePopularCurrencies.value = StateCurrency(isLoading = true)
                }

                is Resources.Success -> {
                    if (it.data!!.success){
                        _statePopularCurrencies.value = StateCurrency(topCurrenciesRates = mapTopCurrencies(it.data))
                    }
                    else {
                        _statePopularCurrencies.value = StateCurrency(error = it.data.error.info)
                    }
                }

                is Resources.Error -> {
                    _statePopularCurrencies.value = StateCurrency(error = it.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getExchangeRate(from: String, to: String) {
        getExchangeRateUseCase(from, to).onEach {
            when (it) {
                is Resources.Loading -> {
                    _stateExchange.value = StateCurrency(isLoading = true)
                }
                is Resources.Success -> {
                    if (it.data!!.success){
                        _stateExchange.value = StateCurrency(exchangeRate = calculateExchangeRate(it.data))
                    }
                    else {
                        _stateExchange.value = StateCurrency(error = it.data.error.info)
                    }
                }
                is Resources.Error -> {
                    _stateExchange.value = StateCurrency(error = it.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun mapTopCurrencies(exchange: CurrencyExchange):Map<String,Double>{
        val topCurrenciesRates: Map<String, Double> =
            exchange.rates.entries.toList().associate { entry->
                entry.key to entry.value / exchange.rates.values.toList()[0]
            }
        return topCurrenciesRates
    }
    private fun calculateExchangeRate(exchange: CurrencyExchange): Double{
        val ratesList = exchange.rates.values.toList()
        return if (ratesList.count() == 1) 1.0 else ratesList[1] / ratesList[0]
    }
    data class StateCurrency(
        val isLoading: Boolean = false,
        val topCurrenciesRates: Map<String, Double>? = null,
        val exchangeRate: Double? = null,
        val error: String? = null
    )
}