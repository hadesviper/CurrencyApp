package com.herald.currencyapp.common

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
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

    fun getSpecificDate(backDays:Int = 0):String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("America/New_York")
        return dateFormat.format(
            Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -1*backDays)
            }.time
        )
    }
}