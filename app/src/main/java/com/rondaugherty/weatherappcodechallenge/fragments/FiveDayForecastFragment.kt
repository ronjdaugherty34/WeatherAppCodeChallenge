package com.rondaugherty.weatherappcodechallenge.fragments


import android.location.Location
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
import com.rondaugherty.weatherappcodechallenge.LocationHelper
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.adapter.WeatherAdapter
import com.rondaugherty.weatherappcodechallenge.repository.WeatherRepository
import com.rondaugherty.weatherappcodechallenge.viewmodel.WeatherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.act


class FiveDayForecastFragment : Fragment(), AnkoLogger{
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
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_five_day_forecast, container, false)
        weatherRecyclerView = view.find(R.id.weatherRecyclerView)
        weatherRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            ) as RecyclerView.LayoutManager?
        }

        val disposable = locationHelper.getLocation(act)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                info("the location helper found $it ")

                info("in main act pusing location")
                weatherRepository.getWeather(it)
                getWeatherViewModel(it)

            }
        compositeDisposable.add(disposable)


        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getWeatherViewModel(location: Location) =
        weatherViewModel.getFiveDayForecast(location).observe(this, Observer { daysList ->
            info("the five day observed conditions are $daysList")

            daysList?.let {
                weatherAdapter = WeatherAdapter(daysList)
                weatherRecyclerView.adapter = weatherAdapter
            }

        })




}
