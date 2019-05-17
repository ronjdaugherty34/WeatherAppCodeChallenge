package com.rondaugherty.weatherappcodechallenge.fragments


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.LocationHelper
import com.rondaugherty.weatherappcodechallenge.adapter.WeatherAdapter
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton


class FiveDayForecastFragment : Fragment(), AnkoLogger {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private lateinit var weatherViewModel: WeatherViewModel
    private val weatherRepository: WeatherRepository = WeatherRepository()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherRecyclerView: RecyclerView
    private val locationHelper: LocationHelper = LocationHelper()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_five_day_forecast, container, false)
        weatherRecyclerView = view.find(R.id.weatherRecyclerView)
        weatherRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
        }

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        val disposable = locationHelper.getLocation(act)
            .subscribeOn(Schedulers.io())
            .subscribe {
                weatherRepository.getFiveDayConditions(it.latitude, it.longitude)
                getWeatherForecastViewModel(it.latitude, it.longitude)

            }
        compositeDisposable.add(disposable)

        getLocation()


        return view
    }

    private fun getLocation() {
        val isNetworkAvaiable = locationHelper.isNetworkAvaiable(act)

        if (isNetworkAvaiable) {

            val disposable = locationHelper.getLocation(act)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    weatherRepository.getFiveDayConditions(it.latitude, it.longitude)
                    getWeatherForecastViewModel(it.latitude, it.longitude)

                }
            compositeDisposable.add(disposable)
        } else {
            showNoNetworkAlert()
        }


    }


    private fun getWeatherForecastViewModel(lat: Double, lon: Double) {
        weatherViewModel.getFiveDayForecast(lat, lon).observe(this, Observer { daysList ->

            daysList?.let {
                weatherAdapter = WeatherAdapter(daysList, act)
                weatherRecyclerView.adapter = weatherAdapter
            }


        })
    }

    private fun showNoNetworkAlert() {
        alert("Network unavailable ") {
            message = "Network need for app to work properly"
            yesButton {

                getLocation()
            }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        getLocation()

    }


}
