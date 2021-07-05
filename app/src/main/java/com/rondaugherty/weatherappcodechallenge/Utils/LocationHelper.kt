package com.rondaugherty.weatherappcodechallenge.Utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class LocationHelper {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun getLocation(context: Context) {
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun getLastKnownLocation(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
            }
    }


//    fun checkPermission(context: Context): Boolean {
//        return PermissionChecker.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//
//        ) == PermissionChecker.PERMISSION_GRANTED
//    }
//
//    suspend fun getLocation(context: Context): LiveData<Location> {
//        return liveData {
//            if (checkPermission(context)) {
//                val fusedLocationProviderClient =
//                    LocationServices.getFusedLocationProviderClient(context)
//                fusedLocationProviderClient.setMockMode(true)
//                fusedLocationProviderClient.lastLocation
//                    .addOnSuccessListener { location: Location? ->
//                        location?.let {
//                            // emitter.onNext(it)
//                            emit(it)
//                        }
//                    }
//            }
//        }
//
////        return BehaviorSubject.create { emitter ->
////            if (checkPermission(context)) {
////                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
////                fusedLocationProviderClient.setMockMode(true)
////                fusedLocationProviderClient.lastLocation
////                    .addOnSuccessListener { location: Location? ->
////                        location?.let {
////                            emitter.onNext(it)
////                        }
////                    }
////            }
////
////        }
//
//    }
//
//    fun isNetworkAvaiable(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetworkInfo = connectivityManager.activeNetworkInfo
//        return when {
//            activeNetworkInfo != null && activeNetworkInfo.isConnected -> true
//            else -> false
//        }
//    }


}