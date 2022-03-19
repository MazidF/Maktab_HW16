package com.example.hw16.data.local

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.hw16.data.local.db.MyDao
import com.example.hw16.data.local.db.UserDao
import com.example.hw16.model.User
import com.example.hw16.model.UserWithTasks
import kotlinx.coroutines.flow.Flow
import java.lang.StringBuilder

class UserDataSource(
    private val userDao: UserDao
) : LocalDataSource<User, String>(userDao) {

    suspend fun getUserWithTasks(): Flow<List<UserWithTasks>> {
        return userDao.getUserWithTasks()
    }

    suspend fun filter(
        name: String? = null,
        password: String? = null,
    ) : Flow<List<User>> {
        return userDao.filter(name, password)
    }
}

