package com.herald.currencyapp.domain.usecases

import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.common.Utils
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.repository.RetroRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val retroRepo: RetroRepo
) {
    operator fun invoke(
        from: String, to: String
    ): Flow<Resources<List<CurrencyExchange>>> = flow {
        try {
            val dates = listOf(
                Utils.getSpecificDate(1),
                Utils.getSpecificDate(2),
                Utils.getSpecificDate(3)
            )

            val data = dates.map { date ->
                retroRepo.getExchangeRate(date = date, from = from, to = to)
            }.toList()

            emit(Resources.Success(data))
        } catch (e: HttpException) {
            emit(Resources.Error(message = e.message.toString()))
        } catch (e: IOException) {
            emit(Resources.Error(message = e.message.toString()))
        }
    }
}