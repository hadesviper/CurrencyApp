package com.herald.currencyapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.herald.currencyapp.R
import com.herald.currencyapp.common.Utils
import com.herald.currencyapp.databinding.FragmentConversionBinding
import com.herald.currencyapp.presentation.viewmodels.AllCurrenciesViewModel
import com.herald.currencyapp.presentation.viewmodels.ConversionViewModel

class FragmentConversion : Fragment(R.layout.fragment_conversion) {

    private lateinit var binding: FragmentConversionBinding
    private val allCurrenciesViewModel: AllCurrenciesViewModel by activityViewModels()
    private val conversionViewModel: ConversionViewModel by activityViewModels()
    private lateinit var progressDialog: AlertDialog
    private var exchangeRate: Double = 1.0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConversionBinding.inflate(layoutInflater)
        binding.fragmentConversion = this

        progressDialog = Utils.loadingDialog(requireContext())

        setUpAllCurrenciesVM()
        setUpExchangeRateVM()

        setUpEditTexts()

        setUpSpinners(spinner = binding.spinnerFrom)
        setUpSpinners(spinner = binding.spinnerTo)

        return binding.root
    }

    fun swapFromTo() {
        binding.run {
            spinnerTo.tag = false
            spinnerFrom.tag = false
            var indexSp1 = spinnerFrom.selectedItemPosition
            var indexSp2 = spinnerTo.selectedItemPosition
            indexSp1 += indexSp2
            indexSp2 = indexSp1 - indexSp2
            indexSp1 -= indexSp2
            spinnerFrom.setSelection(indexSp1)
            spinnerTo.setSelection(indexSp2)
            exchangeRate = 1 / exchangeRate
            edtTextFrom.setText(String.format("%.4f", 1.0))
            edtTextTo.setText(String.format("%.4f", (edtTextFrom.text.toString().toDouble() * exchangeRate)))
        }
    }

    fun navigateToDetails() {
        val action = FragmentConversionDirections.actionFragmentConversionToFragmentDetails(
            binding.spinnerFrom.selectedItem.toString(),
            binding.spinnerTo.selectedItem.toString()
        )
        findNavController().navigate(action)

    }

    private fun setUpSpinners(spinner: Spinner) {
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.i("TAG", "onItemSelected: ${Utils.getSpecificDate()}")
                //I am going to do this check in order to ignore the first invocation which is performed automatically,
                //I sat the tag = true for the first spinner already because I wanted to get the exchange rate for the first time
                if (spinner.tag == true) {
                    Log.i("TAG", "onItemSelected: $p2")
                    conversionViewModel.getExchangeRate(
                        binding.spinnerFrom.selectedItem.toString(),
                        binding.spinnerTo.selectedItem.toString()
                    )
                } else {
                    spinner.tag = true
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }


    private fun setUpEditTexts() {
        binding.run {
            edtTextTo.addTextChangedListener {
                if (edtTextTo.hasFocus()) {
                    if (it.isNullOrEmpty()) {
                        edtTextTo.setText("0")
                    } else {
                        edtTextFrom.setText(String.format("%.4f", (it.toString().toDouble() / exchangeRate)))
                    }
                }
            }
            edtTextFrom.addTextChangedListener {
                if (edtTextFrom.hasFocus()) {
                    if (it.isNullOrEmpty()) {
                        edtTextFrom.setText("0")
                    } else {
                        edtTextTo.setText(String.format("%.4f", (it.toString().toDouble() * exchangeRate)))
                    }
                }
            }
        }
    }


    private fun setUpAllCurrenciesVM() {
        allCurrenciesViewModel.state.observe(viewLifecycleOwner) {
            it.isLoading.run {
                if (this) progressDialog.show() else progressDialog.dismiss()
            }
            it.currencies?.run {
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    this.symbols.keys.toList()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.run {
                    //Kindly read the comment in setUpSpinners() in order to understand the following line
                    spinnerTo.adapter = adapter
                    spinnerFrom.adapter = adapter
                    spinnerTo.setSelection(1)
                    spinnerFrom.tag = true
                    Log.i("TAG", "setUpAllCurrenciesVM: ${spinnerFrom.selectedItem}")
                }
            }
            it.error?.run {
                Utils.showErrorDialog(requireContext(), this) {
                    allCurrenciesViewModel.getAllCurrencies()
                }
            }
        }
    }

    private fun setUpExchangeRateVM() {
        conversionViewModel.stateExchange.observe(viewLifecycleOwner) {
            it.isLoading.run {
                if (this) progressDialog.show() else progressDialog.dismiss()
            }
            it.exchangeRate?.run {
                exchangeRate = this
                binding.edtTextTo.setText(String.format("%.4f", (binding.edtTextFrom.text.toString().toDouble() * exchangeRate)))
            }
            it.error?.run {
                Utils.showErrorDialog(requireContext(), this) {
                    conversionViewModel.getExchangeRate(
                        binding.spinnerFrom.selectedItem.toString(),
                        binding.spinnerTo.selectedItem.toString()
                    )
                }
            }
        }
    }
}