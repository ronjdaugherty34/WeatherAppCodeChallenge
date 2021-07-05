package com.rondaugherty.weatherappcodechallenge.model

data class Days (
    val day: String,
    val forecastList: List<CurrentConditions>
)