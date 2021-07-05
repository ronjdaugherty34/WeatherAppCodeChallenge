package com.rondaugherty.weatherappcodechallenge.fragments


import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.*
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class CurrentTempFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by viewModels<WeatherViewModel>()
    private lateinit var bindingCurrentTempFragment: CurrentTempFragment
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


        // requestPermissionLauncher.launch()


//        dateTimeTextView = view.find(R.id.dateTimeTextView)
//        forecastTemp = view.find(R.id.forecastTemp)
//        weatherIconImageView = view.find(R.id.weatherIconImageView)weatherIconImageView


        screenSetup()

        weatherObserver()

        return view
    }


    private fun weatherObserver() {
        weatherViewModel.weatherCurrentLiveData.observe(
            viewLifecycleOwner,
            Observer { currentConditions ->

                if (currentConditions == null) {
                    forecastTemp.invisible()
                    weatherIconImageView.invisible()
                }
                currentConditions?.let {
                    forecastTemp.visible()
                    weatherIconImageView.visible()
                    icon = currentConditions.weather[0].icon

                val path = "https://openweathermap.org/img/w/$icon.png"
                val uri = Uri.parse(path)

                weatherIconImageView.loadImg(uri.toString())

                dateTimeTextView.text = getString(
                    R.string.date_text,
                    (currentConditions.dt.toLong()).convertLongToMonthDay(currentConditions.dt.toLong() * 1000)
                )

                forecastTemp.text = getString(R.string.temp, currentConditions.main.temp.roundToInt().toString())


            }


        })


    }


    private fun screenSetup() {
//        val isNetworkAvaiable = locationHelper.isNetworkAvaiable(act)
//        val hasPermissions = locationHelper.checkPermission(act)
//        info("permissions are $hasPermissions")
//        info("permissions are $isNetworkAvaiable")
//
//        dateTimeTextView.text = getString(R.string.network_message)
//        forecastTemp.visibility = View.INVISIBLE
//        weatherIconImageView.visibility = View.INVISIBLE
//
//        when (hasPermissions) {
//            true -> {
//                getLocation(isNetworkAvaiable)
//            }
//            false -> {
//                requestPermissions()
//            }
//        }


    }

//    private fun getLocation(isNetworkAvailable: Boolean) {
//        if (isNetworkAvailable) {
//
//            val disposable = locationHelper.getLocation(act)
//                .subscribeOn(Schedulers.io())
//                .subscribe {
//                    weatherRespository.getWeather(it.latitude, it.longitude)
//                    weatherObserver(it.latitude, it.longitude)
//
//
//                }
//            compositeDisposable.add(disposable)
//        } else {
//            dateTimeTextView.text = getString(R.string.network_message)
//            forecastTemp.invisible()
//            weatherIconImageView.invisible()
//            showNoNetworkAlert()
//        }
//
//    }

//    private fun requestPermissions() {
//        val rxPermissions = RxPermissions(this)
//
//        val disposable = rxPermissions.requestEachCombined(
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_NETWORK_STATE
//        )
//            .debounce(1, TimeUnit.SECONDS)
//            .subscribe { permission ->
//                when {
//                    permission.granted -> {
//                        permissionGranted = permission.name
//                        info("per name ${permission.name}")
//                        val isNetworkAvaiable = locationHelper.isNetworkAvaiable(act)
//                        getLocation(isNetworkAvaiable)
//
//
//                    }
//
//                    permission.shouldShowRequestPermissionRationale -> {
//                        // Denied permission without ask never again
//                        showAlert()
//
//                    }
//                    else -> {
//                        // Denied permission with ask never again
//                        // Need to go to the settings
//                        showDenialAlert()
//
//                    }
//                }
//            }
//
//        compositeDisposable.add(disposable)
//    }
//
//    private fun showNoNetworkAlert() {
//        alert("Network unavailable ") {
//            message = "Network need for app to work properly"
//            yesButton {
//                val thing = locationHelper.isNetworkAvaiable(act)
//                getLocation(thing)
//            }
//        }.show()
//    }

//    private fun showAlert() {
//        alert("Location Permission") {
//            message = "Need location permission to retrieve the weather data"
//            yesButton { requestPermissions() }
//            noButton { }
//
//
//        }.show()
//    }
//
//    private fun showDenialAlert() {
//        alert("Permission denied") {
//            message = "Critical permission needed denied"
//            yesButton { requestPermissions() }
//            noButton { }
//        }.show()
//    }

    override fun onResume() {
        super.onResume()
        screenSetup()

    }


}
