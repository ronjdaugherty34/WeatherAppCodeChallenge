package com.rondaugherty.weatherappcodechallenge.fragments



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.LocationHelper
import com.rondaugherty.weatherappcodechallenge.adapter.WeatherAdapter
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_five_day_forecast.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton


class FiveDayForecastFragment : Fragment(), AnkoLogger {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherAdapter: WeatherAdapter
    private val locationHelper: LocationHelper = LocationHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_five_day_forecast, container, false)

        weatherRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
        }

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        getWeatherForecastObserver()

        getLocation()

        return view
    }

    private fun getLocation() {
        val isNetworkAvaiable = locationHelper.isNetworkAvaiable(requireContext())

        if (isNetworkAvaiable) {
            val disposable = locationHelper.getLocation(act)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    weatherViewModel.setLatLon(it.latitude, it.longitude)
                }
            compositeDisposable.add(disposable)
        } else {
            showNoNetworkAlert()
        }
    }


    private fun getWeatherForecastObserver() {
        weatherViewModel.fiveDayForecastFlow.observe(viewLifecycleOwner, Observer { daysList ->
            daysList?.let {
                weatherAdapter = WeatherAdapter(daysList, requireContext())
                if (::weatherAdapter.isInitialized) {
                    weatherRecyclerView.adapter = weatherAdapter
                }

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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
