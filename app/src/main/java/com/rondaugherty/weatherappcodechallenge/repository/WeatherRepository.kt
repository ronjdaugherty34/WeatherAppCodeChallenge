package com.rondaugherty.weatherappcodechallenge.repository

import com.rondaugherty.weatherappcodechallenge.Utils.Utils
import com.rondaugherty.weatherappcodechallenge.Utils.convertLongToMonthDay
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import com.rondaugherty.weatherappcodechallenge.networking.WebService
import io.reactivex.Observable
import io.reactivex.Observable.create
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

import retrofit2.Response

class WeatherRepository {
    // private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    suspend fun getWeather(lat: Double, lon: Double): Response<CurrentConditions> {

        return WebService.getAPIService().getCurrentConditions(
            lat = lat,
            lon = lon,
            units = "imperial",
            apiKey = Utils.APPID
        )

//        return create { emitter ->
//
//            val disposable = WebService.getAPIService().getCurrentConditions(
//                lat = lat,
//                lon = lon,
//                units = "imperial",
//                apiKey = Utils.APPID
//            )
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribeBy(
//                    onSuccess = ({ response ->
//                        response.body()?.let { it ->
//                            emitter.onNext(it)
//                        }
//                    }),
//                    onError = ({ wtf("Error with fetching weather data response ${it.printStackTrace()}") }),
//                    onComplete = ({ info("fetching weather data complete") })
//
//                )
//            compositeDisposable.add(disposable)
//        }


    }

    suspend fun getFiveDayConditions(lat: Double, lon: Double): Response<FiveDayForecast> {
        return WebService.getAPIService().getFiveDayForecast(
            lat = lat,
            lon = lon,
            units = "imperial",
            apiKey = Utils.APPID
        )

//        return create { emitter ->
//            val disposable = WebService.getAPIService().getFiveDayForecast(
//                lat = lat,
//                lon = lon,
//                units = "imperial",
//                apiKey = Utils.APPID
//            )
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribeBy(
//                    onSuccess = ({ response ->
//                        response.body()?.let {
//                            emitter.onNext(sortConditions(it))
//                        }
//
//                    }),
//                    onError = ({ wtf("Error with fetching 5 day data response ${it.printStackTrace()}") }),
//                    onComplete = ({ info("fetching 5 day data complete") })
//
//                )
//
//            compositeDisposable.add(disposable)
//        }

    }


    internal fun sortConditions(fiveDayForecast: FiveDayForecast): List<Days> {
        val dayList = mutableListOf<Days>()
        val oneDayList = fiveDayForecast.forecastList
            .groupBy { (it.dt.toLong()).convertLongToMonthDay(it.dt.toLong() * 1000) }

        for ((k, v) in oneDayList) {
            dayList.add(Days(k, v))
        }
        return dayList
    }



}