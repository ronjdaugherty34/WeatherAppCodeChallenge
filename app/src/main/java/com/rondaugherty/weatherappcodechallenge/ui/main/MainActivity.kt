package com.rondaugherty.weatherappcodechallenge.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.CustomViewPager
import com.rondaugherty.weatherappcodechallenge.adapter.SectionsPagerAdapter
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger



class MainActivity : AppCompatActivity(), AnkoLogger {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: CustomViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        viewPager.setPagingEnabled(false)

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

    }
}