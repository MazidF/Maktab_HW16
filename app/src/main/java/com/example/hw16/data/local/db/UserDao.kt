package com.example.hw16.data.local.db

import androidx.room.Dao
import androidx.room.Query
import com.example.hw16.model.Task
import com.example.hw16.model.User
import com.example.hw16.model.UserWithTasks

@Dao
interface UserDao : MyDao<User, String> {

    @Query("delete from user_table")
    override fun deleteAll()

    @Query("select * from user_table where user_name = :primaryKey")
    override fun find(primaryKey: String) : User?

    @Query("select * from user_table")
    override fun getAll() : List<User>

    @Query("select * from user_table")
    fun getUserWithTasks() : List<UserWithTasks>

    @Query("select * from user_table " +
            "where user_name = :name " +
            "and user_password = :password " +
            "and 1"
    ) fun filter(
        name: String? = null,
        password: String? = null,
    ) : List<User>
}