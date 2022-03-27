package com.example.hw16.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw16.domain.TaskAndUserUseCase

class MyViewModelFactory(private val serviceLocator: MyServiceLocator) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ViewModelMain::class.java)) {
//            return ViewModelMain(serviceLocator.repository) as T
//        }
        return modelClass
            .getConstructor(TaskAndUserUseCase::class.java)
            .newInstance(serviceLocator.taskAndUserUseCase)
    }
}
