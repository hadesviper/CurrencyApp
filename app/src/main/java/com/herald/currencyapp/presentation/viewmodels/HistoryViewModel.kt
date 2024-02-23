package com.herald.currencyapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.usecases.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {


    private val _stateHistory = MutableLiveData<StateHistory>()
    val stateHistory: LiveData<StateHistory> = _stateHistory


    fun getHistory(from: String, to: String) {
        getHistoryUseCase(from, to).onEach {
            when (it) {
                is Resources.Loading -> {
                    _stateHistory.value = StateHistory(isLoading = true)
                }

                is Resources.Success -> {
                    _stateHistory.value = StateHistory(currencies = it.data!!)
                }

                is Resources.Error -> {
                    _stateHistory.value = StateHistory(error = it.message)
                }
            }
        }.launchIn(viewModelScope)
    }



    data class StateHistory(
        val isLoading: Boolean = false,
        val currencies: List<CurrencyExchange>? = null,
        val error: String? = null
    )

}