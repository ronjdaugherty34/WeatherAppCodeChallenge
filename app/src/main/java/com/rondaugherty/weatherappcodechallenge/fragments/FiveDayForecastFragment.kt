package com.rondaugherty.weatherappcodechallenge.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info


class FiveDayForecastFragment : Fragment(), AnkoLogger{
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fiveDayTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_five_day_forecast, container, false)
        fiveDayTextView = view.find(R.id.fiveDayTextView)
        getWeatherViewModel()
        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    }

    private fun getWeatherViewModel() =
        weatherViewModel.getFiveDayForecast().observe(this, Observer { currentConditions ->
            (info { "about to set textview $currentConditions" })

            if (currentConditions != null) {
                fiveDayTextView .text = currentConditions.toString()
            } else {
                (info { "about to set textview $currentConditions" })
            }


        })


}
