package com.example.sltodolist.internal

import android.annotation.SuppressLint
import android.app.Application
import com.example.sltodolist.internal.app.AppComponentHolder

@SuppressLint("StaticFieldLeak")
object DI {

    private lateinit var application: Application

    val app: AppComponentHolder by lazy {
        AppComponentHolder(application)
    }

    fun init(application: Application){
        this.application = application
    }
}