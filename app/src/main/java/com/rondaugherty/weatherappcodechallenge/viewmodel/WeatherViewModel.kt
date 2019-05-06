package com.rondaugherty.weatherappcodechallenge.viewmodel

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
import org.jetbrains.anko.wtf

class WeatherViewModel : ViewModel(), AnkoLogger {

    private val weatherRepository: WeatherRepository = WeatherRepository()
   internal val weatherCurrentLiveData: MutableLiveData<CurrentConditions> = MutableLiveData()
    internal  val weatherFiveDayLiveData: MutableLiveData<List<Days>> = MutableLiveData()
    internal val compositeDisposable = CompositeDisposable()


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

    fun getFiveDayForecast(lat: Double, lon: Double): MutableLiveData<List<Days>> {
        val disposable = weatherRepository.getFiveDayConditions(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy {
                info { "about to post value to current  5 day conditions" }
                weatherFiveDayLiveData.postValue(it)
            }

        compositeDisposable.add(disposable)

        return weatherFiveDayLiveData
    }



    fun clearObservers() {
        compositeDisposable.clear()
    }


}