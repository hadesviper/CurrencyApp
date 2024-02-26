package com.herald.currencyapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.usecases.GetAllCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AllCurrenciesViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StateAllCurrencies>()
    val state: LiveData<StateAllCurrencies> = _state

    init {
        getAllCurrencies()
    }

    fun getAllCurrencies() {
        getAllCurrenciesUseCase().onEach {
            when (it) {
                is Resources.Loading -> {
                    _state.value = StateAllCurrencies(isLoading = true)
                }

                is Resources.Success -> {
                    _state.value = StateAllCurrencies(currencies = it.data!!)
                }

                is Resources.Error -> {
                    _state.value = StateAllCurrencies(error = it.message)
                }
            }
        }.launchIn(viewModelScope)
    }
    data class StateAllCurrencies (
        val isLoading:Boolean = false,
        val currencies:AllCurrencies? = null,
        val error:String? = null
    )
}