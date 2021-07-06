package com.rondaugherty.weatherappcodechallenge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.model.Days


class WeatherAdapter(private val dayList: List<Days>, val context: Context) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.parent_recycler, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun getItemCount(): Int = dayList.size


    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val day = dayList[position]
        holder.textView.text = day.day
        holder.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
            adapter = ChildAdapter(day.forecastList, context)
            setRecycledViewPool(viewPool)
        }
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.rv_child)
        var textView: TextView = itemView.findViewById(R.id.parentTextView)
    }
}