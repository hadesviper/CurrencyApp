package com.herald.currencyapp.domain.usecases

import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.repository.RetroRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val retroRepo: RetroRepo
) {
    operator fun invoke(from: String, to: String): Flow<Resources<CurrencyExchange>> = flow {
        try {
            emit(Resources.Loading())
            val data = retroRepo.getExchangeRate(from = from, to = to)
            emit(Resources.Success(data))
        } catch (e: HttpException) {
            emit(Resources.Error(message = e.message.toString()))
        } catch (e: IOException) {
            emit(Resources.Error(message = e.message.toString()))
        }
    }
}