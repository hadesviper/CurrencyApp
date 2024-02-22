package com.herald.currencyapp.domain.usecases

import com.herald.currencyapp.common.Constants
import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.repository.RetroRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetCurrencyDetailsUseCase @Inject constructor(
    private val retroRepo: RetroRepo
) {
    operator fun invoke(
        date: String = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).format(Calendar.getInstance().time), from: String
    ): Flow<Resources<CurrencyExchange>> = flow {
        try {
            emit(Resources.Loading())
            val data = retroRepo.getExchangeRate(date = date,from = from, to = Constants.Top_Currencies)
            emit(Resources.Success(data))
        } catch (e: HttpException) {
            emit(Resources.Error(message = e.message.toString()))
        } catch (e: IOException) {
            emit(Resources.Error(message = e.message.toString()))
        }
    }
}