package com.rondaugherty.weatherappcodechallenge.repository

import com.rondaugherty.weatherappcodechallenge.Utils.Utils
import com.rondaugherty.weatherappcodechallenge.Utils.convertLongToMonthDay
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.rondaugherty.weatherappcodechallenge.model.Days
import com.rondaugherty.weatherappcodechallenge.model.FiveDayForecast
import com.rondaugherty.weatherappcodechallenge.networking.WebService
import retrofit2.Response

class WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): Response<CurrentConditions> {
        return WebService.getAPIService().getCurrentConditions(
            lat = lat,
            lon = lon,
            units = "imperial",
            apiKey = Utils.APPID
        )
    }

    suspend fun getFiveDayConditions(lat: Double, lon: Double): Response<FiveDayForecast> {
        return WebService.getAPIService().getFiveDayForecast(
            lat = lat,
            lon = lon,
            units = "imperial",
            apiKey = Utils.APPID
        )
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