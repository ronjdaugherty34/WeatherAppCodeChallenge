package com.rondaugherty.weatherappcodechallenge.fragments


import android.Manifest
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
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import java.util.concurrent.TimeUnit


class FiveDayForecastFragment : Fragment(), AnkoLogger{
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private lateinit var weatherViewModel: WeatherViewModel
    private val weatherRepository: WeatherRepository = WeatherRepository()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherRecyclerView: RecyclerView
    private val locationHelper: LocationHelper = LocationHelper()
    private var permissionGranted = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_five_day_forecast, container, false)
        weatherRecyclerView = view.find(R.id.weatherRecyclerView)
        weatherRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
        }

        val disposable = locationHelper.getLocation(act)
            .subscribeOn(Schedulers.io())
            .subscribe {
                weatherRepository.getFiveDayConditions(it.latitude, it.longitude)
                getWeatherViewModel(it.latitude, it.longitude)

            }
        compositeDisposable.add(disposable)

        screenSetUp()


        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    }

    private fun screenSetUp(){
        if (!locationHelper.checkPermission(act)) {
            requestPermissions()
        } else {
            getLocation()
        }
    }

    private fun getLocation (){
        if (locationHelper.isNetworkAvaiable(act)) {
            val disposable = locationHelper.getLocation(act)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    weatherRepository.getWeather(it.latitude, it.longitude)
                    getWeatherViewModel(it.latitude, it.longitude)

                }
            compositeDisposable.add(disposable)
        }else {
            showNoNetworkAlert()
        }

    }

    private fun requestPermissions() {
        val rxPermissions = RxPermissions(this)

        val disposable = rxPermissions.requestEachCombined(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
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

    private fun showNoNetworkAlert() {
        alert("Network unavailable ") {
            message = "Network need for app to work properly"
            yesButton {getLocation ()  }
        }.show()
    }

    private fun showAlert() {
        alert("Location Permissions Needed") {
            message = "Location permissions are need for app to function properly "
            yesButton { requestPermissions() }
            noButton { }
        }.show()
    }

    private fun showDenialAlert() {
        alert("Denial permissions") {
            message = "Denied permission that is needed for app to function properly"
            yesButton { }
            noButton { }
        }.show()
    }

    private fun getWeatherViewModel(lat : Double, lon: Double) =
        weatherViewModel.getFiveDayForecast(lat, lon).observe(this, Observer { daysList ->
            info("the five day observed conditions are $daysList")

            daysList?.let {
                weatherAdapter = WeatherAdapter(daysList, act)
                weatherRecyclerView.adapter = weatherAdapter
            }

        })




}
