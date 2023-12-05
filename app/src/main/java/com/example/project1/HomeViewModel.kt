package com.example.project1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class HomeViewModel : ViewModel() {

    private val _navController = MutableLiveData<NavController?>().apply {
        value = null
    }
    val navController: LiveData<NavController?> = _navController


    fun setNav(navController: NavController){
        _navController.value = navController
    }
}