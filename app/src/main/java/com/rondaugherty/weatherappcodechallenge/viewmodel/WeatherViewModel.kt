package com.rondaugherty.weatherappcodechallenge.viewmodel

import androidx.lifecycle.*
import com.rondaugherty.weatherappcodechallenge.Utils.convertLongToMonthDay
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import com.rondaugherty.weatherappcodechallenge.model.UserLocation
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class WeatherViewModel : ViewModel() {

    private val weatherRepository: WeatherRepository = WeatherRepository()
    private val _weatherCurrentLiveData: MutableLiveData<CurrentConditions> = MutableLiveData()
    val weatherCurrentLiveData: LiveData<CurrentConditions> = _weatherCurrentLiveData
    private val _weatherFiveDayLiveData: MutableLiveData<List<Days>> = MutableLiveData()
    val weatherFiveDayLiveData: LiveData<List<Days>> = _weatherFiveDayLiveData
    private val _locationLiveData = MutableLiveData<UserLocation>()
    val locationLiveData: LiveData<UserLocation> = _locationLiveData
    private val _permissionState = MutableStateFlow(false)
    val permissionState: StateFlow<Boolean> = _permissionState

    fun setPermissionState(hasPermissions: Boolean) {
        _permissionState.value = hasPermissions
    }

    fun setLocationLiveData(lat: Double, lon: Double) {
        _locationLiveData.value = UserLocation(lat, lon)
    }

    fun getCurrentWeather() {
        viewModelScope.launch {
            val response = runCatching {
                withContext(Dispatchers.IO) {
                    _locationLiveData.value?.latitude?.let {
                        _locationLiveData.value?.longitude?.let { it1 ->
                            weatherRepository.getWeather(
                                it,
                                it1
                            )
                        }
                    }
                }
            }

            response.onSuccess { thing ->
                thing?.body()?.let {
                    _weatherCurrentLiveData.value = it
                    Timber.d("ron the weather is ${_weatherCurrentLiveData.value}")
                }
            }
            response.onFailure {

            }
        }
    }

    fun getFiveDayForecast() {
        viewModelScope.launch {
            val response = kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    _locationLiveData.value?.latitude?.let {
                        _locationLiveData.value?.longitude?.let { it1 ->
                            weatherRepository.getFiveDayConditions(
                                it, it1
                            )
                        }
                    }
                }
            }

            response.onSuccess { it ->
                it?.body()?.let { fiveDayForecast ->
                    _weatherFiveDayLiveData.value = sortConditions(fiveDayForecast)
                }
            }
            response.onFailure { }
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