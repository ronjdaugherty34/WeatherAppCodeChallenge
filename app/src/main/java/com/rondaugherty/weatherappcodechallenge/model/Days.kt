package com.rondaugherty.weatherappcodechallenge.model

data class Days (
    var day : String,
    var forecastList :List< CurrentConditions>
)