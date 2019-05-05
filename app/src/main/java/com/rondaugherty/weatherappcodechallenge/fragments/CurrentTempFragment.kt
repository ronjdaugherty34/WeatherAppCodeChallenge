package com.rondaugherty.weatherappcodechallenge.fragments


import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rondaugherty.weatherappcodechallenge.LocationHelper
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.RxBus
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import java.util.concurrent.TimeUnit


class CurrentTempFragment : Fragment(), AnkoLogger {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherTextView: TextView
    private val locationHelper: LocationHelper by lazy { LocationHelper() }
    private var permissionGranted = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val disposable = RxBus.listen(Location::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                getWeatherViewModel()
            }

        compositeDisposable.add(disposable)


    }

    private fun getWeatherViewModel() =
        weatherViewModel.getCurrentWeather().observe(this, Observer { currentConditions ->
            (info { "about to set textview $currentConditions" })

            if (currentConditions != null) {
                weatherTextView.text = currentConditions.toString()
            } else {
                (info { "about to set textview $currentConditions" })
            }


        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_temp, container, false)

        weatherTextView = view.find(R.id.weatherTextView)

        if (!locationHelper.checkPermission(act)) {
            requestPermissions()
        } else {
            toast("We have permissions")
            val disposable = locationHelper.getLocation(act)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    info("the location helper found $it ")

                    info("in main act pusing location")
                    RxBus.publish(it)
                    getWeatherViewModel()

                }
            compositeDisposable.add(disposable)
        }


        // Inflate the layout for this fragment


        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

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
