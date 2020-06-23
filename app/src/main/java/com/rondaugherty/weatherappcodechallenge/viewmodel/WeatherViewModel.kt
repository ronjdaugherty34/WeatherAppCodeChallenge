package com.rondaugherty.weatherappcodechallenge.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rondaugherty.weatherappcodechallenge.Utils.Utils
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
import com.rondaugherty.weatherappcodechallenge.networking.WebService
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.wtf

class WeatherViewModel : ViewModel(), AnkoLogger {

    private val weatherRepository: WeatherRepository = WeatherRepository()
    internal val weatherCurrentLiveData: MutableLiveData<CurrentConditions> = MutableLiveData()
    internal val weatherFiveDayLiveData: MutableLiveData<List<Days>> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private val latLiveData = MutableLiveData<Double>()
    private val longLiveData = MutableLiveData<Double>()


    fun getCurrentWeather(lat: Double, lon: Double): MutableLiveData<CurrentConditions> {
        val disposable = weatherRepository.getWeather(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onNext = ({ currentConditions ->
                    weatherCurrentLiveData.postValue(currentConditions)
                }),
                onError = ({ wtf("Error with fetching weather data response ${it.printStackTrace()}") }),
                onComplete = ({ info("fetching weather data complete") })
            )
        compositeDisposable.add(disposable)

        return weatherCurrentLiveData
    }

    fun getCurrentWeatherFlow(): kotlinx.coroutines.flow.Flow<CurrentConditions> {
     return flow {
        if (viewModelScope.isActive){
            //val result = kotlin.runCatching {  }
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


    }

    fun getFiveDayForecast(lat: Double, lon: Double): MutableLiveData<List<Days>> {
        val disposable = weatherRepository.getFiveDayConditions(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy { weatherFiveDayLiveData.postValue(it) }

        compositeDisposable.add(disposable)

        return weatherFiveDayLiveData
    }


    fun clearObservers() {
        compositeDisposable.clear()
    }




}