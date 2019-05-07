package com.rondaugherty.weatherappcodechallenge.fragments


import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.DateFormatter
import com.rondaugherty.weatherappcodechallenge.Utils.LocationHelper
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class CurrentTempFragment : Fragment(), AnkoLogger {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var weatherViewModel: WeatherViewModel
    private val weatherRespository: WeatherRepository = WeatherRepository()
    private lateinit var dateTimeTextView: TextView
    private lateinit var forecastTemp: TextView
    private lateinit var weatherIconImageView: ImageView
    private val locationHelper: LocationHelper = LocationHelper()
    private var permissionGranted = ""
    private var icon = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_temp, container, false)

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        dateTimeTextView = view.find(R.id.dateTimeTextView)
        forecastTemp = view.find(R.id.forecastTemp)
        weatherIconImageView = view.find(R.id.weatherIconImageView)

        screenSetup()

        return view
    }


    private fun getWeatherViewModel(lat: Double, lon: Double) {
        weatherViewModel.getCurrentWeather(lat, lon).observe(this, Observer { currentConditions ->

            if (currentConditions == null) {
                forecastTemp.visibility = View.INVISIBLE
                weatherIconImageView.visibility = View.INVISIBLE
            }
            currentConditions?.let {
                forecastTemp.visibility = View.VISIBLE
                weatherIconImageView.visibility = View.VISIBLE
                icon = currentConditions.weather[0].icon

                val path = "https://openweathermap.org/img/w/$icon.png"
                val uri = Uri.parse(path)


                Glide.with(act)
                    .load(uri)
                    .into(weatherIconImageView)

                dateTimeTextView.text = getString(
                    R.string.date_text,
                    DateFormatter.convertLongToMonthDay(currentConditions.dt.toLong() * 1000)
                )

                forecastTemp.text = getString(R.string.temp, currentConditions.main.temp.roundToInt().toString())


            }


        })
    }


    private fun screenSetup() {
        val isNetworkAvaiable = locationHelper.isNetworkAvaiable(act)
        val hasPermissions = locationHelper.checkPermission(act)
        info("permissions are $hasPermissions")
        info("permissions are $isNetworkAvaiable")

        dateTimeTextView.text = getString(R.string.network_message)
        forecastTemp.visibility = View.INVISIBLE
        weatherIconImageView.visibility = View.INVISIBLE




        when (hasPermissions) {
            true -> {
                getLocation(isNetworkAvaiable)
            }
            false -> {
                requestPermissions()
            }
        }


    }

    private fun getLocation(isNetworkAvaiable: Boolean) {


        if (isNetworkAvaiable) {
            val disposable = locationHelper.getLocation(act)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    weatherRespository.getWeather(it.latitude, it.longitude)
                    getWeatherViewModel(it.latitude, it.longitude)


                }
            compositeDisposable.add(disposable)
        } else {
            dateTimeTextView.text = getString(R.string.network_message)
            forecastTemp.visibility = View.INVISIBLE
            weatherIconImageView.visibility = View.INVISIBLE
            showNoNetworkAlert()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        weatherRespository.clearObservers()
        weatherViewModel.clearObservers()


    }

    private fun requestPermissions() {
        val rxPermissions = RxPermissions(this)

        val disposable = rxPermissions.requestEachCombined(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
            .debounce(1, TimeUnit.SECONDS)
            .subscribe { permission ->
                when {
                    permission.granted -> {
                        permissionGranted = permission.name
                        info("per name ${permission.name}")
                        val isNetworkAvaiable = locationHelper.isNetworkAvaiable(act)
                        getLocation(isNetworkAvaiable)


                    }

                    permission.shouldShowRequestPermissionRationale -> {
                        // Denied permission without ask never again
                        showAlert()

                    }
                    else -> {
                        // Denied permission with ask never again
                        // Need to go to the settings
                        showDenialAlert()

                    }
                }
            }

        compositeDisposable.add(disposable)
    }

    private fun showNoNetworkAlert() {
        alert("Network unavailable ") {
            message = "Network need for app to work properly"
            yesButton {
                val thing = locationHelper.isNetworkAvaiable(act)
                getLocation(thing)
            }
        }.show()
    }

    private fun showAlert() {
        alert("Location Permission") {
            message = "Need location permission to retrieve the weather data"
            yesButton { requestPermissions() }
            noButton { }


        }.show()
    }

    private fun showDenialAlert() {
        alert("Permission denied") {
            message = "Critical permission needed denied"
            yesButton { requestPermissions() }
            noButton { }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        screenSetup()

    }


}
