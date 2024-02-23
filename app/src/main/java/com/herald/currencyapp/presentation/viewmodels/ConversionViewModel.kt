package com.herald.currencyapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.usecases.GetCurrencyDetailsUseCase
import com.herald.currencyapp.domain.usecases.GetExchangeRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val getCurrencyDetailsUseCase: GetCurrencyDetailsUseCase,
    private val getExchangeRateUseCase: GetExchangeRateUseCase
) : ViewModel() {

    private val _stateCurrencyDetails = MutableLiveData<StateCurrency>()
    val stateCurrencyDetails: LiveData<StateCurrency> = _stateCurrencyDetails


    private val _stateExchange = MutableLiveData<StateCurrency>()
    val stateExchange: LiveData<StateCurrency> = _stateExchange

    init {
    }

    fun getCurrencyDetails(currency: String) {
        getCurrencyDetailsUseCase(currency).onEach {
            when (it) {
                is Resources.Loading -> {
                    _stateCurrencyDetails.value = StateCurrency(isLoading = true)
                }

                is Resources.Success -> {
                    _stateCurrencyDetails.value = StateCurrency(currencies = it.data!!)
                }

                is Resources.Error -> {
                    _stateCurrencyDetails.value = StateCurrency(error = it.message)
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
                    _stateExchange.value = StateCurrency(currencies = it.data!!)
                }

                is Resources.Error -> {
                    _stateExchange.value = StateCurrency(error = it.message)
                }
            }
        }.launchIn(viewModelScope)
    }


    data class StateCurrency(
        val isLoading: Boolean = false,
        val currencies: CurrencyExchange? = null,
        val error: String? = null
    )
}