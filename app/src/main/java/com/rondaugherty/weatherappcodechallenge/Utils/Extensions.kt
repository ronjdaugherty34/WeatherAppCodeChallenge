package com.rondaugherty.weatherappcodechallenge.Utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


fun ImageView.loadImg(imageUrl: String) = Glide.with(context).load(imageUrl).into(this)



fun Long.convertLongToMonthDay(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("EEE, MMM d")
    return format.format(date)
}

fun Long.convertLongToTimeHours(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("h aa")
    return format.format(date)
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}
