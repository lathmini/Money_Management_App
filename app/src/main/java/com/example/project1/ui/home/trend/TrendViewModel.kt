package com.example.project1.ui.home.trend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrendViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "TODO"
    }
    val text: LiveData<String> = _text
}