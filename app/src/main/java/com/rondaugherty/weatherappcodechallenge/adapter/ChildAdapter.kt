package com.rondaugherty.weatherappcodechallenge.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.DateFormatter
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import kotlinx.android.synthetic.main.weather_item_row.view.*
import org.jetbrains.anko.AnkoLogger
import kotlin.math.roundToInt

class ChildAdapter(private val children: List<CurrentConditions>, val context: Context) : RecyclerView.Adapter<ChildAdapter.ViewHolder>(),
    AnkoLogger {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val child = children[position]
        val icon = children[position].weather[0].icon

        val path = "https://openweathermap.org/img/w/$icon.png"
        val uri = Uri.parse(
            path
        )
        Glide.with(context)
            .load(uri)
            .into(holder.itemView.fiveDayWeatherIconImageView)

        holder.textView2.text = "${DateFormatter.convertLongToTimeHours(child.dt.toLong() * 1000)}"
        holder.textView3.text = "${child.main.temp.roundToInt()}°"

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val textView2: TextView = itemView.fiveTimeTextView
        val textView3: TextView = itemView.fiveDayForecastTemp


    }
}