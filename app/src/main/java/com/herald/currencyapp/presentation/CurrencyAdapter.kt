package com.herald.currencyapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.herald.currencyapp.databinding.RecyclerCurrenciesBinding

class CurrencyAdapter(private val currencyList: Map<String,Double>) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(private val binding: RecyclerCurrenciesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(label: String = "",amount: Double = 0.0){
            binding.currencyName.text = label
            binding.exchangeRate.text = String.format("%.3f",amount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = RecyclerCurrenciesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currentCurrency = currencyList.entries.toList()[position]
        holder.bind(currentCurrency.key,currentCurrency.value)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

}