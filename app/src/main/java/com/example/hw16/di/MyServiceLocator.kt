package com.example.hw16.di

import android.content.Context
import com.example.hw16.data.MyRepository
import com.example.hw16.data.local.FileLocalDataSource
import com.example.hw16.data.local.TaskDataSource
import com.example.hw16.data.local.UserDataSource
import com.example.hw16.data.local.db.MyDatabase
import java.util.concurrent.Executors

class MyServiceLocator(context: Context) {
    private val database = MyDatabase.getDatabase(context)
    private val executors = Executors.newFixedThreadPool(3)
    private val userDao = database.getUserDao()
    private val userDataSource = UserDataSource(userDao)
    private val taskDao = database.getTaskDao()
    private val taskDataSource = TaskDataSource(taskDao)
    private val fileDatabase = FileLocalDataSource()
    val repository = MyRepository(executors, userDataSource, taskDataSource, fileDatabase)
}
