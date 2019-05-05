package com.rondaugherty.weatherappcodechallenge.repository

import android.location.Location
import com.rondaugherty.weatherappcodechallenge.Utils.RxBus
import com.rondaugherty.weatherappcodechallenge.Utils.Utils
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import com.rondaugherty.weatherappcodechallenge.networking.WebService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.wtf

class WeatherRepository : AnkoLogger {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    var lon = 0.0
    var lat = 0.0


    private fun getWeather() = RxBus.listen(Location::class.java)
        .subscribeOn(Schedulers.io())
        .subscribeBy {
            lon = it.longitude
            lat = it.latitude

            info("rxbus found $lon and $lat")

        }

    fun getCurrentConditions(): Observable<CurrentConditions> {

        info { "in getConditions" }
        getWeather()

        return BehaviorSubject.create<CurrentConditions> { emitter ->

            val disposable = WebService.getAPIService().getCurrentConditions(
                lat = lat,
                lon = lon,
                units = "imperial",
                apiKey = Utils.APPID
            )
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = ({ response ->

                        info("${response.body()}")

                        response.body()?.let { it ->
                            emitter.onNext(it)
                        }


                    }),
                    onError = ({ wtf("Error with fetching weather data response ${it.printStackTrace()}") }),
                    onComplete = ({ info("fetching weather data complete") })

                )
            compositeDisposable.add(disposable)


        }


    }


    fun getFiveDayConditions(): Observable<FiveDayForecast> {

        info { "in 5 getConditions" }
        getWeather()

        return BehaviorSubject.create { emitter ->
            val disposable = WebService.getAPIService().getFiveDayForecast(
                lat = lat,
                lon = lon,
                units = "imperial",
                apiKey = Utils.APPID
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = ({ response ->
                        response.body()?.let { emitter.onNext(it) }
                    }),
                    onError = ({ wtf("Error with fetching 5 day data response ${it.printStackTrace()}") }),
                    onComplete = ({ info("fetching 5 day data complete") })

                )

            compositeDisposable.add(disposable)
        }

    }

    fun clearObservers() {
        compositeDisposable.clear()


    }

}