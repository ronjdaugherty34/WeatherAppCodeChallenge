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
import com.rondaugherty.weatherappcodechallenge.LocationHelper
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.DateFormatter
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_temp, container, false)

        dateTimeTextView = view.find(R.id.dateTimeTextView)
        forecastTemp = view.find(R.id.forecastTemp)
        weatherIconImageView = view.find(R.id.weatherIconImageView)

        screenSetup()

        return view
    }


    private fun getWeatherViewModel(lat : Double, lon: Double) =
        weatherViewModel.getCurrentWeather(lat, lon).observe(this, Observer { currentConditions ->

            currentConditions?.let {
                icon = currentConditions.weather[0].icon

                val path = "https://openweathermap.org/img/w/$icon.png"
                val uri = Uri.parse(path)


                Glide.with(act)
                    .load(uri)
                    .into(weatherIconImageView)

                dateTimeTextView.text = getString(R.string.date_text,
                    DateFormatter.convertLongToMonthDay(currentConditions.dt.toLong() * 1000))

                forecastTemp.text = getString(R.string.temp, currentConditions.main.temp.roundToInt().toString())



            }


        })


    private fun screenSetup() {
        if (!locationHelper.checkPermission(act)) {
            requestPermissions()
        } else {
            val disposable = locationHelper.getLocation(act)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    weatherRespository.getWeather(it.latitude, it.longitude)
                    getWeatherViewModel(it.latitude, it.longitude)

                }
            compositeDisposable.add(disposable)
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
            Manifest.permission.ACCESS_FINE_LOCATION
        )
            .debounce(1, TimeUnit.SECONDS)
            .subscribe { permission ->
                when {
                    permission.granted -> {
                        permissionGranted = permission.name
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

    private fun showAlert() {
        alert("need permissions") {
            message = "need permissions rationale"
            yesButton { requestPermissions() }
            noButton { }


        }.show()
    }

    private fun showDenialAlert() {
        alert("need permissions") {
            message = "denied permission"
            yesButton { }
            noButton { }
        }.show()
    }
}
