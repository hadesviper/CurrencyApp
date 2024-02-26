package com.herald.currencyapp.common

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    //Change the day and month to your testing date, please.
    private var day = 26
    private var month = "02"
    private var today     = "2024-$month-$day"
    private var yesterday = "2024-$month-${day - 1}"
    private var daybefore = "2024-$month-${day - 2}"
    @Test
    fun `get today's Date`() {
        val result = Utils.getSpecificDate()
        assertEquals(result,today)
    }

    @Test
    fun `get yesterday's Date`() {
        val result = Utils.getSpecificDate(1)
        assertEquals(result,yesterday)
    }

    @Test
    fun `get the day before yesterday's Date`() {
        val result = Utils.getSpecificDate(2)
        assertEquals(result,daybefore)
    }

}