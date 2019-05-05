package com.rondaugherty.weatherappcodechallenge

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class LocationHelper : AnkoLogger {
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    fun checkPermission(context: Context): Boolean {
        return PermissionChecker.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION

        ) == PermissionChecker.PERMISSION_GRANTED
    }

    fun getLocation(context: Context): Observable<Location> {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return BehaviorSubject.create { emitter ->
            if (checkPermission(context)) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->

                        location?.let {
                            emitter.onNext(it)
//                            RxBus.publish(it)

                        }
                        info("location in successlistner  $location")


                    }
            }

        }

    }
}