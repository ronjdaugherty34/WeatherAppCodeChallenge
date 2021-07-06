package com.rondaugherty.weatherappcodechallenge.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.convertLongToMonthDay
import com.rondaugherty.weatherappcodechallenge.Utils.invisible
import com.rondaugherty.weatherappcodechallenge.Utils.loadImg
import com.rondaugherty.weatherappcodechallenge.Utils.visible
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import kotlin.math.roundToInt

class CurrentTempFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var dateTimeTextView: TextView
    private lateinit var forecastTemp: TextView
    private lateinit var weatherIconImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_current_temp, container, false)

        dateTimeTextView = view.findViewById(R.id.dateTimeTextView)
        forecastTemp = view.findViewById(R.id.forecastTemp)
        weatherIconImageView = view.findViewById<ImageView>(R.id.weatherIconImageView)

        weatherObserver()
        return view
    }

    private fun weatherObserver() {
        weatherViewModel.weatherCurrentLiveData.observe(
            viewLifecycleOwner,
            { currentConditions ->

                if (currentConditions == null) {
                    forecastTemp.invisible()
                    weatherIconImageView.invisible()
                }
                currentConditions?.let {

                    forecastTemp.visible()
                    weatherIconImageView.visible()
                    val icon = currentConditions.weather[0].icon

                    val path = "https://openweathermap.org/img/w/$icon.png"
                    val uri = Uri.parse(path)

                    weatherIconImageView.loadImg(uri.toString())

                    dateTimeTextView.text = getString(
                        R.string.date_text,
                        (currentConditions.dt.toLong()).convertLongToMonthDay(currentConditions.dt.toLong() * 1000)
                    )

                    forecastTemp.text = getString(
                        R.string.temp,
                        currentConditions.main.temp.roundToInt().toString()
                    )

                }
            })
    }
}
