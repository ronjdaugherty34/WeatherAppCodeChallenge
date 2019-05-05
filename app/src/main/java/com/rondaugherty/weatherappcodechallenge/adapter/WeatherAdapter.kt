package com.rondaugherty.weatherappcodechallenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.model.Days
import kotlinx.android.synthetic.main.parent_recycler.view.*
import org.jetbrains.anko.AnkoLogger

class WeatherAdapter (private var dayList: List<Days>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>(),
    AnkoLogger {

    private val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder{
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.parent_recycler,parent , false)
        return WeatherViewHolder(itemView)
    }

    override fun getItemCount(): Int = dayList.size


    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {

        val day = dayList[position]
        holder.textView.text = day.day
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(holder.recyclerView.context, LinearLayout.HORIZONTAL, false)
            adapter = ChildAdapter(day.forecastList)
            setRecycledViewPool(viewPool)
        }


    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView : RecyclerView = itemView.rv_child
        val textView: TextView = itemView.parentTextView

    }
}