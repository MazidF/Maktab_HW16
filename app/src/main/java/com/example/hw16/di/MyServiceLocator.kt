package com.example.hw16.di

import android.content.Context
import com.example.hw16.data.MyRepository
import com.example.hw16.data.local.db.MyDatabase
import java.util.concurrent.Executors

class MyServiceLocator(context: Context) {
    private val database = MyDatabase.getDatabase(context)
    private val executors = Executors.newFixedThreadPool(3)
    private val userDao = database.getUserDao()
    private val taskDao = database.getTaskDao()
    val repository = MyRepository(executors, userDao, taskDao)
}