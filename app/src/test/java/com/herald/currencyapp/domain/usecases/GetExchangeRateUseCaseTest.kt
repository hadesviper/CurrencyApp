package com.herald.currencyapp.domain.usecases

import com.herald.currencyapp.common.Constants
import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.repository.RetroRepo
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class GetExchangeRateUseCaseTest {


    @Mock
    private lateinit var mockRetroRepo: RetroRepo

    private lateinit var getExchangeRateUseCase: GetExchangeRateUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getExchangeRateUseCase = GetExchangeRateUseCase(mockRetroRepo)
    }

    @Test
    fun `invoke success`() = runBlocking {
        val fromCurrency = "AED"
        val toCurrency = "AFN"
        val mockData = CurrencyExchange(success = true, base = "EUR", date = "2024-02-23", rates = mapOf(
            "AED" to 3.979144,
            "AFN" to 78.548246,
            "ALL" to 103.840919,
            "AMD" to 439.949015,
            "ANG" to 1.952867,
            "AOA" to 900.821845
        ))
        `when`(mockRetroRepo.getExchangeRate(date = Constants.Latest_Date, from = fromCurrency, to =  toCurrency)).thenReturn(mockData)

        val result = getExchangeRateUseCase(fromCurrency, toCurrency).toList()

        assertEquals(2, result.size) // Loading and Success
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Success)
        assertEquals(mockData, (result[1] as Resources.Success).data)
    }

    @Test
    fun `invoke API Error`() = runBlocking {
        val errorMessage = "An error has occurred"

        val fromCurrency = "AED"
        val toCurrency = "AFN"
        val mockData = CurrencyExchange(success = false, base = "EUR", date = "2024-02-23", rates = mapOf(
            "AED" to 3.979144,
            "AFN" to 78.548246,
            "ALL" to 103.840919,
            "AMD" to 439.949015,
            "ANG" to 1.952867,
            "AOA" to 900.821845
        ), CurrencyExchange.ApiError(
            code = 0, type = "", info = errorMessage

        ))
        `when`(mockRetroRepo.getExchangeRate(date = Constants.Latest_Date, from = fromCurrency, to =  toCurrency)).thenReturn(mockData)

        val result = getExchangeRateUseCase(fromCurrency, toCurrency).toList()

        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)
        assertEquals(errorMessage, (result[1] as Resources.Error).message)
    }

    @Test
    fun `invoke HttpException error`() = runBlocking {
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        `when`(mockRetroRepo.getExchangeRate(date = Constants.Latest_Date, from = fromCurrency, to =  toCurrency))
            .thenAnswer { throw HttpException(Response.error<Any>(404, ResponseBody.create(null,""))) }

        val result = getExchangeRateUseCase(fromCurrency, toCurrency).toList()

        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)
        assertEquals("HTTP 404 Response.error()", (result[1] as Resources.Error).message)
    }

    @Test
    fun `invoke IOException error`() = runBlocking {
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        `when`(mockRetroRepo.getExchangeRate(date = Constants.Latest_Date, from = fromCurrency, to =  toCurrency))
            .thenAnswer { throw IOException("Network error") }

        val result = getExchangeRateUseCase(fromCurrency, toCurrency).toList()

        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)
        assertEquals("Network error", (result[1] as Resources.Error).message)
    }
}