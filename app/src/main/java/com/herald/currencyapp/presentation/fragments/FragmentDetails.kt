package com.herald.currencyapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.herald.currencyapp.R
import com.herald.currencyapp.common.Utils
import com.herald.currencyapp.databinding.FragmentDetailsBinding
import com.herald.currencyapp.presentation.BarChartView
import com.herald.currencyapp.presentation.CurrencyAdapter
import com.herald.currencyapp.presentation.viewmodels.ConversionViewModel
import com.herald.currencyapp.presentation.viewmodels.HistoryViewModel

class FragmentDetails : Fragment(R.layout.fragment_details) {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var barChart: BarChartView
    private val args: FragmentDetailsArgs by navArgs()
    private lateinit var progressDialog: AlertDialog
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val conversionViewModel: ConversionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        progressDialog = Utils.loadingDialog(requireContext())
        barChart = BarChartView(requireContext())
        binding.barChart.addView(barChart)
        "From ${args.from} to ${args.to}".also { binding.txtFromTo.text = it }
        setUpHistoryViewModel()
        setUpConversionViewModel()
        return binding.root
    }

    private fun setUpConversionViewModel() {
        conversionViewModel.statePopularCurrencies.observe(viewLifecycleOwner) {
            it.isLoading.run {
                if (this) progressDialog.show() else progressDialog.dismiss()
            }
            it.topCurrenciesRates?.run {
                val adapterCurrencies = CurrencyAdapter(this)
                binding.recyclerPopular.adapter = adapterCurrencies
            }
            it.error?.run {
                Utils.showErrorDialog(requireContext(), this) {
                    conversionViewModel.getPopularCurrencies(args.from)
                }
            }
        }
        conversionViewModel.getPopularCurrencies(args.from)
    }

    private fun setUpHistoryViewModel() {
        historyViewModel.stateHistory.observe(viewLifecycleOwner) { history ->
            history.isLoading.run {
                if (this) progressDialog.show() else progressDialog.dismiss()
            }
            history.currencyHistory?.run {
                this.forEach {
                    barChart.addBar(BarChartView.Bar(it.value, it.key))
                }
                val adapterHistory = CurrencyAdapter(this)
                binding.recyclerHistory.adapter = adapterHistory
            }
            history.error?.run {
                Utils.showErrorDialog(requireContext(), this) {
                    historyViewModel.getHistory(args.from, args.to)
                }
            }
        }
        historyViewModel.getHistory(args.from, args.to)
    }
}
