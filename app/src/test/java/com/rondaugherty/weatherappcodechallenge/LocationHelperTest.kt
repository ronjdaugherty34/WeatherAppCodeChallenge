package com.rondaugherty.weatherappcodechallenge

import android.content.Context
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock





class LocationHelperTest {

    private val locationHelper: LocationHelper by lazy { spyk<LocationHelper>() }


    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun checkPermission() {
        val context = mock(Context::class.java)
        every { locationHelper.checkPermission(context) } returns true
        every { locationHelper.checkPermission(context) } returns false

        Truth.assert_().that(locationHelper.checkPermission(context)).isNotNull()


        locationHelper.checkPermission(context)

        verify { locationHelper.checkPermission(context) }


    }




}