package com.rondaugherty.weatherappcodechallenge.networking

import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("weather?")
    fun getCurrentConditions(@Query("lat")  lat:Double,
                             @Query("lon") lon: Double,
                             @Query("units")
                             units : String,
                             @Query("APPID")  apiKey: String
    ) : Maybe<Response<CurrentConditions>>

    @GET("forecast?")
    fun getFiveDayForecast(@Query("lat")  lat:Double,
                           @Query("lon") lon: Double,
                           @Query("units")
                           units : String,
                           @Query("APPID")  apiKey: String
    ) : Maybe<Response<FiveDayForecast>>

}