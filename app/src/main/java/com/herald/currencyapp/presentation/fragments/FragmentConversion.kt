package com.herald.currencyapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.herald.currencyapp.R
import com.herald.currencyapp.databinding.FragmentConversionBinding

class FragmentConversion: Fragment(R.layout.fragment_conversion) {

    private lateinit var binding:FragmentConversionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConversionBinding.inflate(layoutInflater)
        return binding.root
    }
}