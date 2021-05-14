package com.example.curiosityonline.extensions

import androidx.lifecycle.MutableLiveData

// set default value
fun <T : Any?> MutableLiveData<T>.default(initValue: T) = apply{ value = initValue }

// set new value
fun <T> MutableLiveData<T>.set(newValue: T) = apply{ value = newValue }