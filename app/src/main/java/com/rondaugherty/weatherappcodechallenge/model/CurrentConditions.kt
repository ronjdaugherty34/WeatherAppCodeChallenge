package com.rondaugherty.weatherappcodechallenge.model

import com.google.gson.annotations.SerializedName

data class FiveDayForecast (
    @SerializedName("list")
    var forecastList : List<CurrentConditions>
)
data class CurrentConditions(
    @SerializedName("clouds")


    var clouds: Clouds,
    @SerializedName("cod")
    var cod: Int,
    @SerializedName("coord")
    var coord: Coord,
    @SerializedName("dt")
    var dt: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("main")
    var main: Main,
    @SerializedName("name")
    var name: String,
    @SerializedName("rain")
    var rain: Rain,
    @SerializedName("sys")
    var sys: Sys,
    @SerializedName("weather")
    var weather: List<Weather>,
    @SerializedName("wind")
    var wind: Wind
)

data class Sys(
    @SerializedName("country")
    var country: String,
    @SerializedName("sunrise")
    var sunrise: Int,
    @SerializedName("sunset")
    var sunset: Int
)

data class Rain(
    @SerializedName("3h")
    var h: Double
)

data class Weather(
    @SerializedName("description")
    var description: String,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("main")
    var main: String
)

data class Clouds(
    @SerializedName("all")
    var all: Int
)

data class Coord(
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lon")
    var lon: Double
)

data class Main(
    @SerializedName("humidity")
    var humidity: Int,
    @SerializedName("pressure")
    var pressure: Double,
    @SerializedName("temp")
    var temp: Double,
    @SerializedName("temp_max")
    var tempMax: Double,
    @SerializedName("temp_min")
    var tempMin: Double
)

data class Wind(
    @SerializedName("deg")
    var deg: Double,
    @SerializedName("speed")
    var speed: Double
)
