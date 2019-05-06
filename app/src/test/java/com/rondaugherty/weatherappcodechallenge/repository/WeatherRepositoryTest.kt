package com.rondaugherty.weatherappcodechallenge.repository

import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private val weatherRepository = spyk<WeatherRepository>(recordPrivateCalls = true)


    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getWeather() {

        val testObserver = TestObserver<CurrentConditions>()
        val lat = 42.43225
        val lon = -83.31018
        val result = weatherRepository.getWeather(lat, lon)

        every { weatherRepository.getWeather(lat, lon) } returns result

        //Test the Observer produces response without errors
        result.subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.hasSubscription()

        //verify that the function was called
        verify(atLeast = 1) { weatherRepository.getWeather(lat, lon) }

        //assert that the function has no null properties
        assertThat(weatherRepository.getWeather(lat, lon)).hasNoNullFieldsOrProperties()
        assertThat(weatherRepository.getWeather(lat, lon)).isNotNull

    }

    @Test
    fun getFiveDayConditions() {
        val testObserver = TestObserver<List<Days>>()
        val lat = 42.43225
        val lon = -83.31018
        val result = weatherRepository.getFiveDayConditions(lat, lon)

        every { weatherRepository.getFiveDayConditions(lat, lon) } returns result

        //Test the Observer produces response without errors
        result.subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.hasSubscription()

        //verify that the function was called
        verify(atLeast = 1) { weatherRepository.getFiveDayConditions(lat, lon) }

        //assert that the function has no null properties
        assertThat(weatherRepository.getFiveDayConditions(lat, lon)).hasNoNullFieldsOrProperties()
        assertThat(weatherRepository.getFiveDayConditions(lat, lon)).isNotNull

    }

    @Test
    fun sortConditions() {
        val weatherRepository = spyk(WeatherRepository(), recordPrivateCalls = true)
        val dayList = mockk<List<Days>>()
        val fiveDayForecast = mockk<FiveDayForecast>()


        //checking if the function returns a list of Days
        every { weatherRepository.sortConditions(fiveDayForecast) } returns dayList

        //verifying that the function is called once
        weatherRepository.sortConditions(fiveDayForecast)
        verify(atMost = 1) { weatherRepository["sortConditions"](fiveDayForecast) }


    }


    @Test
    fun clearObservers() {
    }
}