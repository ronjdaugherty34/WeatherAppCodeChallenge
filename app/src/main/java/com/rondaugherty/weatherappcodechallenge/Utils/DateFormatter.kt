package com.rondaugherty.weatherappcodechallenge.Utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("EEE, MMM d")
        return format.format(date)
    }

    fun convertLongToTimeTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("h aa")
        return format.format(date)
    }

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.parse(date).time
    }
}