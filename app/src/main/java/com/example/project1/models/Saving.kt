package com.example.project1.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Saving (

    @Exclude
    @set:Exclude
    @get:Exclude
    var id:String = "",
    var title: String? = null,
    var amount: Double? = null,
    var date: String? = null,
    var category: String? = null,
)
