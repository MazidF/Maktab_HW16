package com.example.hw16.ui

import android.app.Application
import com.example.hw16.di.MyServiceLocator

class App : Application() {

    companion object {
        lateinit var serviceLocator: MyServiceLocator
        private set
    }

    override fun onCreate() {
        super.onCreate()
        serviceLocator = MyServiceLocator(this)
    }
}