package com.rondaugherty.weatherappcodechallenge.networking

import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("weather?")
    suspend fun getCurrentConditions(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units")
        units: String,
        @Query("APPID") apiKey: String
    ): Response<CurrentConditions>


    @GET("forecast?")
    suspend fun getFiveDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units")
        units: String,
        @Query("APPID") apiKey: String
    ): Response<FiveDayForecast>

}