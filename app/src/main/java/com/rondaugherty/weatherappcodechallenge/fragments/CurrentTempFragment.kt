package com.rondaugherty.weatherappcodechallenge.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rondaugherty.weatherappcodechallenge.LocationHelper
import com.rondaugherty.weatherappcodechallenge.R
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger


class CurrentTempFragment : Fragment() , AnkoLogger {
    private var permissionGranted = ""
    private var permission = false
    private val locationHelper = LocationHelper()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {





        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_temp, container, false)
    }



    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

    }


}
