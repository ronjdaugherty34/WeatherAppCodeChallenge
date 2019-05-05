package com.rondaugherty.weatherappcodechallenge.repository

import android.location.Location
import com.rondaugherty.weatherappcodechallenge.Utils.DateFormatter
import com.rondaugherty.weatherappcodechallenge.Utils.Utils
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
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

    private var lon: Double = 0.0
    private var lat: Double = 0.0



     fun getWeather(location: Location) : Observable<CurrentConditions>  {

         return BehaviorSubject.create<CurrentConditions> { emitter ->

             val disposable = WebService.getAPIService().getCurrentConditions(
                 lat = location.latitude,
                 lon = location.longitude,
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



    fun getFiveDayConditions(location: Location): Observable<List<Days>> {

        info { "in 5 getConditions" }


        info { "in  5 getConditions ${location.longitude} ${location.latitude}" }


        return BehaviorSubject.create { emitter ->
            val disposable = WebService.getAPIService().getFiveDayForecast(
                lat = location.latitude,
                lon = location.longitude,
                units = "imperial",
                apiKey = Utils.APPID
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = ({ response ->
                        response.body()?.let {
                            emitter.onNext(sortConditions(it))
                        }

                    }),
                    onError = ({ wtf("Error with fetching 5 day data response ${it.printStackTrace()}") }),
                    onComplete = ({ info("fetching 5 day data complete") })

                )

            compositeDisposable.add(disposable)
        }

    }

    private fun sortConditions(fiveDayForecast: FiveDayForecast) : List<Days>{
        val dayList = mutableListOf<Days>()
        val oneDayList = fiveDayForecast.forecastList.groupBy { DateFormatter.convertLongToTime(it.dt.toLong() *1000) }
        for ((k, v) in  oneDayList) {
            println("$k = $v")

            dayList.add(Days(k,v))
        }
        return dayList
    }

    fun clearObservers() {
        compositeDisposable.clear()


    }

}