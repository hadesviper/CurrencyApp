package com.herald.currencyapp.domain.usecases

import com.herald.currencyapp.common.Resources
import com.herald.currencyapp.domain.models.AllCurrencies
import com.herald.currencyapp.domain.repository.RetroRepo
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class GetAllCurrenciesUseCaseTest {


    @Mock
    private lateinit var mockRetroRepo: RetroRepo

    private lateinit var getAllCurrenciesUseCase: GetAllCurrenciesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getAllCurrenciesUseCase = GetAllCurrenciesUseCase(mockRetroRepo)

    }

    @Test
    fun getAllCurrencies() = runBlocking {
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
        Mockito.`when`(mockRetroRepo.getAllCurrencies()).thenReturn(expectedAllCurrencies)
        val result = getAllCurrenciesUseCase().toList()
        assertEquals(2, result.size) // Loading and Success
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Success)

        assertEquals(expectedAllCurrencies, (result[1] as Resources.Success).data)
    }

    @Test
    fun `Invoke API Error`() = runBlocking {
        val errorMessage = "An error has occurred"
        val expectedAllCurrencies = AllCurrencies(
            success = false, symbols = mapOf(
                "AED" to "United Arab Emirates Dirham",
                "AFN" to "Afghan Afghani",
                "ALL" to "Albanian Lek",
                "AMD" to "Armenian Dram",
            ),
            error = AllCurrencies.ApiError(
                code = 0,
                type = "",
                info = errorMessage
            )
        )
        Mockito.`when`(mockRetroRepo.getAllCurrencies()).thenReturn(expectedAllCurrencies)
        val result = getAllCurrenciesUseCase().toList()
        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)

        assertEquals(errorMessage, (result[1] as Resources.Error).message)
    }

    @Test
    fun `invoke HttpException error`() = runBlocking {
        Mockito.`when`(mockRetroRepo.getAllCurrencies()).thenAnswer {
            throw HttpException(
                Response.error<Any>(
                    404,
                    ResponseBody.create(null, "")
                )
            )
        }

        val result = getAllCurrenciesUseCase().toList()

        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)
        assertEquals("HTTP 404 Response.error()", (result[1] as Resources.Error).message)
    }

    @Test
    fun `invoke IOException error`() = runBlocking {
        Mockito.`when`(mockRetroRepo.getAllCurrencies())
            .thenAnswer { throw IOException("Network error") }

        val result = getAllCurrenciesUseCase().toList()

        assertEquals(2, result.size) // Loading and Error
        assertTrue(result[0] is Resources.Loading)
        assertTrue(result[1] is Resources.Error)
        assertEquals("Network error", (result[1] as Resources.Error).message)
    }

}