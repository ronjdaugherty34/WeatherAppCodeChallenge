package com.rondaugherty.weatherappcodechallenge.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class WeatherViewModel : ViewModel(), AnkoLogger {

    private val weatherRepository: WeatherRepository = WeatherRepository()
    private val weatherCurrentLiveData: MutableLiveData<CurrentConditions> = MutableLiveData()
    private val weatherFiveDayLiveData: MutableLiveData<FiveDayForecast> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()



//    private val _index = MutableLiveData<Int>()
//    val text: LiveData<String> = Transformations.map(_index) {
//        "Hello world from section: $it"
//    }
//
//    fun setIndex(index: Int) {
//        _index.value = index
//    }

//    init {
//        weatherCurrentLiveData.postValue(getCurrentWeather().value)
//       // weatherFiveDayLiveData.postValue(getFiveDayForecast ().value)
//    }

    fun getCurrentWeather ( ) : MutableLiveData<CurrentConditions> {
        val disposable = weatherRepository.getCurrentConditions()
            .observeOn(Schedulers.io())
            .subscribeBy {
                 info { "about to post value to current conditions" }
                weatherCurrentLiveData.postValue(it)
            }
        compositeDisposable.add(disposable)

        return weatherCurrentLiveData
    }

    fun getFiveDayForecast () : MutableLiveData<FiveDayForecast> {
        val disposable = weatherRepository.getFiveDayConditions()
            .observeOn(Schedulers.io())
            .subscribeBy {
                info { "about to post value to current  5 day conditions" }
                weatherFiveDayLiveData.postValue(it)
            }

        compositeDisposable.add(disposable)

        return  weatherFiveDayLiveData
    }

    fun refresh(){
        weatherCurrentLiveData.postValue(getCurrentWeather().value)
    }
}