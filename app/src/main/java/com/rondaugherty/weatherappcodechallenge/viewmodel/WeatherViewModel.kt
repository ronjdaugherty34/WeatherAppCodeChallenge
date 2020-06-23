package com.rondaugherty.weatherappcodechallenge.viewmodel

import androidx.lifecycle.*
import com.rondaugherty.weatherappcodechallenge.Utils.Utils
import com.rondaugherty.weatherappcodechallenge.Utils.convertLongToMonthDay
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import com.rondaugherty.weatherappcodechallenge.networking.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber

class WeatherViewModel : ViewModel(), AnkoLogger {

    private val latLiveData = MutableLiveData<Double>()
    private val lonLiveData = MutableLiveData<Double>()

    val setLatLon : (lat : Double, lon : Double) -> Unit = {lat, lon ->
        latLiveData.value = lat
        lonLiveData.value = lon
    }

    @ExperimentalCoroutinesApi
    val currentWeatherFlow: LiveData<CurrentConditions> =
        getCurrentWeatherFlow().flowOn(Dispatchers.IO).asLiveData()

    fun getCurrentWeatherFlow(): kotlinx.coroutines.flow.Flow<CurrentConditions> {
        return flow {
            if (viewModelScope.isActive) {
                try {
                    val result = runCatching {
                        withContext(Dispatchers.IO) {
                            WebService.getAPIService().getCurrentConditions(
                                lat = latLiveData.value,
                                lon = lonLiveData.value,
                                units = "imperial",
                                apiKey = Utils.APPID
                            )
                        }
                    }
                    result.onSuccess { response ->
                        if (response.isSuccessful) {
                            emit(requireNotNull(response.body()!!))
                        }
                    }
                    result.onFailure {t ->
                        Timber.wtf(" the error with current conditions response urls ${t.message} ")
                    }
                } catch (e: Exception) {
                    Timber.wtf(" the error with current conditions response urls ${e.message} ")
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    val fiveDayForecastFlow : LiveData<List<Days>> = getFiveDayForecastFlow().flowOn(Dispatchers.IO).map {
        sortConditions(it)
    }.asLiveData()

    fun getFiveDayForecastFlow(): Flow<FiveDayForecast> {
        return flow {
            if (viewModelScope.isActive) {
                try {
                    val result = runCatching {
                        withContext(Dispatchers.IO) {
                            WebService.getAPIService().getFiveDayForecast(
                                lat = latLiveData.value,
                                lon = lonLiveData.value,
                                units = "imperial",
                                apiKey = Utils.APPID
                            )
                        }
                    }
                    result.onSuccess { response ->
                        if (response.isSuccessful) {
                            emit(requireNotNull(response.body()!!))
                        }
                    }
                    result.onFailure {t ->
                        Timber.wtf(" the error with 5 day conditions response urls ${t.message} ")
                    }
                } catch (e: Exception) {
                    Timber.wtf(" the error with 5 day conditions response urls ${e.message} ")
                }
            }
        }
    }

   private fun sortConditions(fiveDayForecast: FiveDayForecast): List<Days> {
        val dayList = mutableListOf<Days>()
        val oneDayList = fiveDayForecast.forecastList
            .groupBy { (it.dt.toLong()).convertLongToMonthDay(it.dt.toLong() * 1000) }

        for ((k, v) in oneDayList) {
            dayList.add(Days(k, v))
        }
        return dayList
    }
}