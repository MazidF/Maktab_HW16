package com.example.hw16.data.local

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.hw16.data.local.db.MyDao
import com.example.hw16.data.local.db.UserDao
import com.example.hw16.model.User
import com.example.hw16.model.UserWithTasks
import java.lang.StringBuilder

class UserDataSource(
    private val userDao: UserDao
) : LocalDataSource<User, String>(userDao) {

    fun getUserWithTasks(): List<UserWithTasks> {
        return userDao.getUserWithTasks()
    }
}

