package com.herald.currencyapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.herald.currencyapp.R
import com.herald.currencyapp.databinding.FragmentDetailsBinding

class FragmentDetails: Fragment(R.layout.fragment_details) {
    private lateinit var binding: FragmentDetailsBinding
    private val args :FragmentDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }
}