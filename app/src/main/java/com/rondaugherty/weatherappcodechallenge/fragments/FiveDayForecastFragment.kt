package com.rondaugherty.weatherappcodechallenge.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.adapter.WeatherAdapter
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel

class FiveDayForecastFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherRecyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_five_day_forecast, container, false)
        weatherRecyclerView = view.findViewById(R.id.weatherRecyclerView)
        weatherRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
        }

        getWeatherForecastObserver()

        return view
    }

    private fun getWeatherForecastObserver() {
        weatherViewModel.weatherFiveDayLiveData.observe(viewLifecycleOwner, { daysList ->
            daysList?.let {
                weatherAdapter = WeatherAdapter(daysList, requireContext())
                takeIf { ::weatherAdapter.isInitialized }.apply {
                    weatherRecyclerView.adapter = weatherAdapter
                }
            }
        })
    }
}
