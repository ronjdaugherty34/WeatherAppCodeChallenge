package com.rondaugherty.weatherappcodechallenge.Utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class TestViewModelObserver<T> : Observer<T> {
    val observedValues = mutableListOf<T?>()
    override fun onChanged(value: T) {
        observedValues.add(value)

    }


}

fun <T> LiveData<T>.testObserver() = TestViewModelObserver<T>().also { observeForever(it) }