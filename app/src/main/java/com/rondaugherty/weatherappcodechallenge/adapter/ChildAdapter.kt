package com.rondaugherty.weatherappcodechallenge.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.DateFormatter
import com.rondaugherty.weatherappcodechallenge.model.CurrentConditions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_item_row.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.math.roundToInt

class ChildAdapter(private val children : List<CurrentConditions>)
    : RecyclerView.Adapter<ChildAdapter.ViewHolder>(), AnkoLogger{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item_row,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val child = children[position]
        val icon = children[position].weather[0].icon

        info { "In the bindviewholer" }
        val path = "https://openweathermap.org/img/w/$icon.png"
        val uri = Uri.parse(
            path )


        Picasso.with(holder.itemView.context).load(uri).into(holder.itemView.fiveDayWeatherIconImageView)

        holder.textView.text = "${DateFormatter.convertLongToTime(child.dt.toLong() *1000)}"
        holder.textView2.text = "${DateFormatter.convertLongToTimeTime(child.dt.toLong() *1000)}"
        holder.textView3.text = "${child.main.temp.roundToInt()}°"

    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){



        val textView : TextView = itemView.fiveDateTimeTextView
        val textView2 : TextView = itemView.fiveTimeTextView
        val textView3 : TextView = itemView.  fiveDayForecastTemp


    }
}