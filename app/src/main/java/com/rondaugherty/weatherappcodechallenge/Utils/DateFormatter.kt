package com.rondaugherty.weatherappcodechallenge.Utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    fun convertLongToMonthDay(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("EEE, MMM d")
        return format.format(date)
    }

    fun convertLongToTimeHours(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("h aa")
        return format.format(date)
    }




}