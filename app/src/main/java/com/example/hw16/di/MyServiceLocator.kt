package com.example.hw16.di

import android.content.Context
import com.example.hw16.data.MyRepository
import com.example.hw16.data.local.FileLocalDataSource
import com.example.hw16.data.local.SubTaskDataSource
import com.example.hw16.data.local.TaskDataSource
import com.example.hw16.data.local.UserDataSource
import com.example.hw16.data.local.db.MyDatabase
import com.example.hw16.domain.TaskAndUserUseCase

class MyServiceLocator(context: Context) {
    private val database = MyDatabase.getDatabase(context)

    private val userDao = database.getUserDao()
    private val userDataSource = UserDataSource(userDao)

    private val taskDao = database.getTaskDao()
    private val taskDataSource = TaskDataSource(taskDao)

    private val subTaskDao = database.getSubTaskDao()
    private val subTaskDataSource = SubTaskDataSource(subTaskDao)

    private val fileDatabase = FileLocalDataSource()

    private val repository = MyRepository(userDataSource, taskDataSource, subTaskDataSource, fileDatabase)
    private val filesDir = context.filesDir
    val taskAndUserUseCase = TaskAndUserUseCase(repository, filesDir)
}
