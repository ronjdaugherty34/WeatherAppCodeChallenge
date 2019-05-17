package com.rondaugherty.weatherappcodechallenge.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.Utils.CustomViewPager
import com.rondaugherty.weatherappcodechallenge.adapter.SectionsPagerAdapter
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find


class MainActivity : AppCompatActivity(), AnkoLogger {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    private val viewPager: CustomViewPager by lazy { find<CustomViewPager>(R.id.view_pager) }
    private val sectionsPagerAdapter : SectionsPagerAdapter by lazy { SectionsPagerAdapter(this, supportFragmentManager)}
    private val  tabs: TabLayout by lazy {find<TabLayout>(R.id.tabs)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(viewPager){
            adapter = sectionsPagerAdapter
            setPagingEnabled(false)
        }

        tabs.setupWithViewPager(viewPager)

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

    }
}