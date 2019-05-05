package com.rondaugherty.weatherappcodechallenge.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class WeatherViewModel : ViewModel(), AnkoLogger {

    private val weatherRepository: WeatherRepository = WeatherRepository()
    private val weatherCurrentLiveData: MutableLiveData<CurrentConditions> = MutableLiveData()
    private val weatherFiveDayLiveData: MutableLiveData<List<Days>> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()


    fun getCurrentWeather ( location: Location) : MutableLiveData<CurrentConditions> {
        val disposable = weatherRepository.getWeather(location)
            .observeOn(Schedulers.io())
            .subscribeBy {
                 info { "about to post value to current conditions" }
                weatherCurrentLiveData.postValue(it)
            }
        compositeDisposable.add(disposable)

        return weatherCurrentLiveData
    }

    fun getFiveDayForecast (location: Location) : MutableLiveData<List<Days>> {
        val disposable = weatherRepository.getFiveDayConditions(location)
            .observeOn(Schedulers.io())
            .subscribeBy {
                info { "about to post value to current  5 day conditions" }
                weatherFiveDayLiveData.postValue(it)
            }

        compositeDisposable.add(disposable)

        return  weatherFiveDayLiveData
    }


}