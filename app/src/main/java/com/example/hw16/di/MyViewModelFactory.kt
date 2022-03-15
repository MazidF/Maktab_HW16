package com.example.hw16.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw16.data.MyRepository
import com.example.hw16.ui.ViewModelMain

class MyViewModelFactory(private val serviceLocator: MyServiceLocator) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ViewModelMain::class.java)) {
//            return ViewModelMain(serviceLocator.repository) as T
//        }
        return modelClass
            .getConstructor(MyRepository::class.java)
            .newInstance(serviceLocator.repository)
    }
}
