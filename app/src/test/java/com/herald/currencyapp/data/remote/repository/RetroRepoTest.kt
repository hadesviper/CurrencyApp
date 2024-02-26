package com.herald.currencyapp.data.remote.repository

import com.herald.currencyapp.data.remote.RetroService
import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.models.CurrencyExchange
import com.herald.currencyapp.domain.repository.RetroRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class RetroRepoTest {

    @Mock
    private lateinit var retroService: RetroService

    private lateinit var retroRepo: RetroRepo

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        retroRepo = RetroImpl(retroService)
    }

    @Test
    fun testGetAllCurrencies() = runBlocking {
        val expectedAllCurrencies = AllCurrencies(
            success = true, symbols = mapOf(
                "AED" to "United Arab Emirates Dirham",
                "AFN" to "Afghan Afghani",
                "ALL" to "Albanian Lek",
                "AMD" to "Armenian Dram",
            ),
            error = AllCurrencies.ApiError(
                code = 0,
                type = "",
                info = ""
            )
        )
        Mockito.`when`(retroService.getAllCurrencies(Mockito.anyString())).thenReturn(expectedAllCurrencies)

        val result = retroRepo.getAllCurrencies()

        assertEquals(expectedAllCurrencies, result)
    }

    @Test
    fun testGetExchangeRate() = runBlocking {
        val accessKey = "key"
        val date = "2024-02-23"
        val from = "EGP"
        val to = "USD"
        val expectedExchangeRate = CurrencyExchange(
            success = true,
            base = "EUR",
            date = date,
            rates = mapOf(
                from to 1.04,
                to to 34.0
            ),
            error = CurrencyExchange.ApiError(
                code = 0,
                type = "",
                info = ""
            )
        )
        Mockito.`when`(retroService.getExchangeRate(date, accessKey, "$from,$to")).thenReturn(expectedExchangeRate)

        val result = retroRepo.getExchangeRate(accessKey, date,from = from, to = to)
        assertEquals(expectedExchangeRate,result )
    }
}