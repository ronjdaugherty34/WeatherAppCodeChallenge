package com.rondaugherty.weatherappcodechallenge.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rondaugherty.weatherappcodechallenge.R
import com.rondaugherty.weatherappcodechallenge.fragments.CurrentTempFragment
import com.rondaugherty.weatherappcodechallenge.fragments.FiveDayForecastFragment

private val TAB_TITLES = listOf<Int>(
    R.string.tab_text_1,
    R.string.tab_text_2
)


class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =

        when (position){
            0 -> CurrentTempFragment()
            1 -> FiveDayForecastFragment()
            else -> {CurrentTempFragment()}
        }

    override fun getCount(): Int = 2


    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }
}