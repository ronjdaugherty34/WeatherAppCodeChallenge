package com.rondaugherty.weatherappcodechallenge.Utils

import android.Manifest
import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger

class LocationHelper : AnkoLogger {

    fun checkPermission(context: Context): Boolean {
        return PermissionChecker.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION

        ) == PermissionChecker.PERMISSION_GRANTED
    }

    fun getLocation(context: Context): Observable<Location> {
        return BehaviorSubject.create { emitter ->
            if (checkPermission(context)) {
                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationProviderClient.setMockMode(true)
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            emitter.onNext(it)
                        }
                    }
            }

        }

    }

    fun isNetworkAvaiable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return when {
            activeNetworkInfo != null && activeNetworkInfo.isConnected -> true
            else -> false
        }
    }


}