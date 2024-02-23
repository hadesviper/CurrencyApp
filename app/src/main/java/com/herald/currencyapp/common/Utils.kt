package com.herald.currencyapp.common

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

object Utils {
    fun showErrorDialog(context: Context, message: String? = "", retry: () -> Unit = {}) {
        AlertDialog.Builder(context).setTitle("Error")
            .setMessage("An unexpected error has occurred\n${message}")
            .setPositiveButton("Retry!") { _: DialogInterface, _: Int ->
                retry()
            }
            .setNegativeButton("Exit!") { _: DialogInterface?, _: Int ->
                (context as AppCompatActivity).finishAffinity()
                exitProcess(0)
            }
            .setCancelable(false)
            .show()
    }

    fun loadingDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
            .setMessage("Data is loading...")
            .setCancelable(false)
            .create()
    }
}