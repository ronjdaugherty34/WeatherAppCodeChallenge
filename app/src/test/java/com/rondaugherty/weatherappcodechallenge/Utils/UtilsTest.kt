package com.rondaugherty.weatherappcodechallenge.Utils

import io.mockk.mockk
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UtilsTest {



    @Test
   fun  `check variables`(){
        val utilsMock = mockk<Utils>()
        mockkObject(Utils)
        val BASE_URL_OPEN = "http://api.openweathermap.org/data/2.5/"
        val APPID = "fe72d66e3f5786d72c71bef91eb42454"

        assertThat(utilsMock.APPID).isEqualTo(APPID)
        assertThat(utilsMock.BASE_URL_OPEN).isEqualTo(BASE_URL_OPEN)

        assertThat(utilsMock.APPID).isNotBlank()
        assertThat(utilsMock.BASE_URL_OPEN).isNotBlank()

        assertThat(utilsMock.APPID).isNotNull()
        assertThat(utilsMock.BASE_URL_OPEN).isNotNull()
    }
}