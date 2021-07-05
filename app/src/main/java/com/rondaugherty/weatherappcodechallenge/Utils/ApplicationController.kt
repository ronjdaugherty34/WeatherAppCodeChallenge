package com.rondaugherty.weatherappcodechallenge.Utils

import android.app.Application
import com.rondaugherty.weatherappcodechallenge.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}