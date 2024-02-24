package com.herald.currencyapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.herald.currencyapp.R
import com.herald.currencyapp.databinding.FragmentDetailsBinding
import com.herald.currencyapp.presentation.BarChartView
import com.herald.currencyapp.presentation.BarChartView.Bar

class FragmentDetails: Fragment(R.layout.fragment_details) {
    private lateinit var binding: FragmentDetailsBinding
    private val args :FragmentDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        val bars = listOf(
            Bar(.05,"2024-02-12"),
            Bar(.003,"2024-02-12"),
            Bar(0.0555,"2024-02-12"),
        )
        val barChart = BarChartView(requireContext())
        barChart.addBars(bars)
        binding.barChart.addView(barChart)

        return binding.root
    }
}