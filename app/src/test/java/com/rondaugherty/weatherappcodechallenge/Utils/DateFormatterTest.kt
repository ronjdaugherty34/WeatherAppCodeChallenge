package com.rondaugherty.weatherappcodechallenge.Utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class DateFormatterTest {

    @Before
    fun setUp() {
    }

    @Test
    fun convertLongToMonthDay() {
        val time = 1557381600
        val dateFormatterMock = mockk<DateFormatter>()
        mockkObject(DateFormatter)

        every { dateFormatterMock.convertLongToMonthDay(time.toLong()) } returns "Monday, May 6"

        dateFormatterMock.convertLongToMonthDay(time.toLong())

        assertThat(dateFormatterMock.convertLongToMonthDay(time.toLong())).isNotEmpty()

        assertThat(dateFormatterMock.convertLongToMonthDay(time.toLong()))

        verify (atLeast = 1) { dateFormatterMock.convertLongToMonthDay(time.toLong()) }


    }

    @Test
    fun convertLongToTimeHours() {

        val time = 1557381600
        val dateFormatterMock = mockk<DateFormatter>()
        mockkObject(DateFormatter)

        every { dateFormatterMock.convertLongToTimeHours(time.toLong()) } returns "5 PM"

        dateFormatterMock.convertLongToTimeHours(time.toLong())

        assertThat(dateFormatterMock.convertLongToTimeHours(time.toLong())).isNotEmpty()



        verify (atLeast = 1) { dateFormatterMock.convertLongToTimeHours(time.toLong()) }
    }
}