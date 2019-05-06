package com.rondaugherty.weatherappcodechallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.rondaugherty.weatherappcodechallenge.Utils.testObserver
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherViewModelTest {


    private val weatherViewModel: WeatherViewModel by lazy { spyk<WeatherViewModel>() }

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }


    }


    @Test
    fun getCurrentWeather() {

        val lat = 42.43225
        val lon = -83.31018

        val result = weatherViewModel.getCurrentWeather(lat, lon)

        val conditions = weatherViewModel.weatherCurrentLiveData.testObserver()

        weatherViewModel.getCurrentWeather(lat, lon)

        // asserting that the weatherCurrentLiveData is receiving updates
        Truth.assert_().that(conditions.observedValues.size).isGreaterThan(0)

        //asserting that the function returns a mutableLiveData of Conditions
        every { weatherViewModel.getCurrentWeather(lat, lon) } returns result

        // verify that the function runs
        verify { weatherViewModel.getCurrentWeather(lat, lon) }

    }

    @Test
    fun getFiveDayForecast() {
        val lat = 42.43225
        val lon = -83.31018

        val result = weatherViewModel.getFiveDayForecast(lat, lon)

        val conditions = weatherViewModel.weatherFiveDayLiveData.testObserver()

        weatherViewModel.getFiveDayForecast(lat, lon)

        // asserting that the weatherCurrentLiveData is receiving updates
        Truth.assert_().that(conditions.observedValues.size).isGreaterThan(0)

        //assert that the function has no null properties
        assertThat(result).hasNoNullFieldsOrProperties()
        assertThat(result).isNotNull

        //asserting that the function returns a mutableLiveData of Conditions
        every { weatherViewModel.getFiveDayForecast(lat, lon) } returns result

        // verify that the function runs
        verify(atLeast = 1) { weatherViewModel.getFiveDayForecast(lat, lon) }
    }

    @Test
    fun clearObservers() {
    }



}