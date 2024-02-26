package com.herald.currencyapp.domain.usecases

import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.common.Utils
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

class GetHistoryUseCaseTest {

    @Mock
    private lateinit var mockRetroRepo: RetroRepo

    private lateinit var getHistoryUseCase: GetHistoryUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getHistoryUseCase = GetHistoryUseCase(mockRetroRepo)
    }

    @Test
    fun `invoke success`() = runBlocking {
        val fromCurrency = "AED"
        val toCurrency = "AFN"

        val dates = listOf(
            Utils.getSpecificDate(1),
            Utils.getSpecificDate(2),
            Utils.getSpecificDate(3)
        )
        val mockData = listOf(
            CurrencyExchange(
                success = true, base = "EUR", date = dates[0], rates = mapOf(
                    "AED" to 3.979144,
                    "AFN" to 78.548246,
                    "ALL" to 103.840919,
                    "AMD" to 439.949015,
                    "ANG" to 1.952867,
                    "AOA" to 900.821845
                )
            ), CurrencyExchange(
                success = true, base = "EUR", date = dates[1], rates = mapOf(
                    "AED" to 3.979144,
                    "AFN" to 78.548246,
                    "ALL" to 103.840919,
                    "AMD" to 439.949015,
                    "ANG" to 1.952867,
                    "AOA" to 900.821845
                )
            ), CurrencyExchange(
                success = true, base = "EUR", date = dates[2], rates = mapOf(
                    "AED" to 3.979144,
                    "AFN" to 78.548246,
                    "ALL" to 103.840919,
                    "AMD" to 439.949015,
                    "ANG" to 1.952867,
                    "AOA" to 900.821845
                )
            )
        )
        dates.map { date ->
            `when`(
                mockRetroRepo.getExchangeRate(
                    date = date,
                    from = fromCurrency,
                    to = toCurrency
                )
            ).thenReturn(mockData[dates.indexOf(date)])
        }.toList()

        val result = getHistoryUseCase(fromCurrency, toCurrency).toList()

        assertEquals(2, result.size) // Loading and Success
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Success)
        assertEquals(mockData, (result[1] as Resources.Success).data)
    }

    @Test
    fun `invoke API Error`() = runBlocking {
        val fromCurrency = "AED"
        val toCurrency = "AFN"
        val errorMessage = "An error has occurred"

        val dates = listOf(
            Utils.getSpecificDate(1),
            Utils.getSpecificDate(2),
            Utils.getSpecificDate(3)
        )
        val mockData = listOf(
            CurrencyExchange(
                success = true, base = "EUR", date = dates[0], rates = mapOf(
                    "AED" to 3.979144,
                    "AFN" to 78.548246,
                    "ALL" to 103.840919,
                    "AMD" to 439.949015,
                    "ANG" to 1.952867,
                    "AOA" to 900.821845
                )
            ), CurrencyExchange(
                success = false, base = "EUR", date = dates[1], rates = mapOf(
                    "AED" to 3.979144,
                    "AFN" to 78.548246,
                    "ALL" to 103.840919,
                    "AMD" to 439.949015,
                    "ANG" to 1.952867,
                    "AOA" to 900.821845
                ),
                CurrencyExchange.ApiError(code = 0, type = "", info = errorMessage)
            ), CurrencyExchange(
                success = true, base = "EUR", date = dates[2], rates = mapOf(
                    "AED" to 3.979144,
                    "AFN" to 78.548246,
                    "ALL" to 103.840919,
                    "AMD" to 439.949015,
                    "ANG" to 1.952867,
                    "AOA" to 900.821845
                )
            )
        )
        dates.map { date ->
            `when`(
                mockRetroRepo.getExchangeRate(
                    date = date,
                    from = fromCurrency,
                    to = toCurrency
                )
            ).thenReturn(mockData[dates.indexOf(date)])
        }.toList()

        val result = getHistoryUseCase(fromCurrency, toCurrency).toList()

        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)
        assertEquals(errorMessage, (result[1] as Resources.Error).message)
    }


    @Test
    fun `invoke HttpException error`() = runBlocking {
        // Arrange
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val dates = listOf(
            Utils.getSpecificDate(1),
            Utils.getSpecificDate(2),
            Utils.getSpecificDate(3)
        )

        dates.map { date ->
            `when`(mockRetroRepo.getExchangeRate(date = date, from = fromCurrency, to = toCurrency))
                .thenAnswer {
                    throw HttpException(
                        Response.error<Any>(
                            404,
                            ResponseBody.create(null, "")
                        )
                    )
                }
        }.toList()

        // Act
        val result = getHistoryUseCase(fromCurrency, toCurrency).toList()

        // Assert
        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)
        assertEquals("HTTP 404 Response.error()", (result[1] as Resources.Error).message)
    }

    @Test
    fun `invoke IOException error`() = runBlocking {
        // Arrange
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val dates = listOf(
            Utils.getSpecificDate(1),
            Utils.getSpecificDate(2),
            Utils.getSpecificDate(3)
        )

        dates.map { date ->
            `when`(mockRetroRepo.getExchangeRate(date = date, from = fromCurrency, to = toCurrency))
                .thenAnswer { throw IOException("Network error") }
        }.toList()

        val result = getHistoryUseCase(fromCurrency, toCurrency).toList()

        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)
        assertEquals("Network error", (result[1] as Resources.Error).message)
    }
}