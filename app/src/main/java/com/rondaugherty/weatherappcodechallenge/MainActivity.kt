package com.rondaugherty.weatherappcodechallenge

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.rondaugherty.weatherappcodechallenge.ui.main.SectionsPagerAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity(), AnkoLogger {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private var permissionGranted = ""
    private val locationHelper : LocationHelper by lazy { LocationHelper() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

       if (!locationHelper.checkPermission(this))  {
           requestPermissions()
       } else {
           toast("We have permissions")
           val disposable =   locationHelper.getLocation(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                info("the location helper found $it ")
            }
           compositeDisposable.add(disposable)
       }
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

    }
}