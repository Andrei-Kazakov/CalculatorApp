package com.example.calculatorapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication<T> {

    fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<T>)

    fun map(value: T)

    abstract class Abstract<T> : Communication<T> {

        private val liveData = MutableLiveData<T>()

        override fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(lifecycleOwner, observer)
        }

        override fun map(value: T) {
            liveData.value = value
        }
    }
}