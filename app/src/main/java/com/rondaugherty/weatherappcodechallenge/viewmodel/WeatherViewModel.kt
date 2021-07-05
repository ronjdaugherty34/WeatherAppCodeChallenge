package com.rondaugherty.weatherappcodechallenge.viewmodel

import android.Manifest
import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import androidx.core.content.PermissionChecker
import androidx.lifecycle.*
import com.google.android.gms.location.LocationServices
import com.rondaugherty.weatherappcodechallenge.Utils.convertLongToMonthDay
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import com.rondaugherty.weatherappcodechallenge.model.UserLocation
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WeatherViewModel : ViewModel() {

    private val weatherRepository: WeatherRepository = WeatherRepository()
    private val _weatherCurrentLiveData: MutableLiveData<CurrentConditions> = MutableLiveData()
    val weatherCurrentLiveData: LiveData<CurrentConditions> = _weatherCurrentLiveData
    private val _weatherFiveDayLiveData: MutableLiveData<List<Days>> = MutableLiveData()
    private val weatherFiveDayLiveData: LiveData<List<Days>> = _weatherFiveDayLiveData
    private val _locationLiveData = MutableLiveData<UserLocation>()
    val locationLiveData: LiveData<UserLocation> = _locationLiveData
    private val _permissionState = MutableStateFlow(false)
    val permissionState: StateFlow<Boolean> = _permissionState


    init {

    }

    fun setPermissionState(hasPermissions: Boolean) {
        _permissionState.value = hasPermissions
    }

    fun setLocationLiveData(lat: Double, lon: Double) {
        _locationLiveData.value = UserLocation(lat, lon)
    }


    fun getCurrentWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = runCatching {
                withContext(Dispatchers.IO) {
                    weatherRepository.getWeather(lat, lon)
                }
            }

            response.onSuccess { thing ->
                thing.body()?.let {
                    _weatherCurrentLiveData.value = it
                }
            }
            response.onFailure {

            }
        }

//        val disposable = weatherRepository.getWeather(lat, lon)
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.io())
//            .subscribeBy(
//                onNext = ({ currentConditions ->
//                    weatherCurrentLiveData.postValue(currentConditions)
//                }),
//                onError = ({ wtf("Error with fetching weather data response ${it.printStackTrace()}") }),
//                onComplete = ({ info("fetching weather data complete") })
//            )
//        compositeDisposable.add(disposable)
//
//        return weatherCurrentLiveData
    }

    fun getFiveDayForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    weatherRepository.getFiveDayConditions(lat, lon)
                }
            }

            response.onSuccess {
                it.body()?.let {
                    _weatherFiveDayLiveData.value = sortConditions(it)
                }
            }
            response.onFailure { }
        }

//        val response = kotlin.runCatching { withContext() }
//        val disposable = weatherRepository.getFiveDayConditions(lat, lon)
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.io())
//            .subscribeBy { weatherFiveDayLiveData.postValue(it) }
//
//        compositeDisposable.add(disposable)
//
//        return weatherFiveDayLiveData
    }


//    fun clearObservers() {
//        compositeDisposable.clear()
//    }


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