package com.example.hw16.data.local.db

import androidx.room.Dao
import androidx.room.Query
import com.example.hw16.model.Task
import com.example.hw16.model.User
import com.example.hw16.model.UserWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : MyDao<User, String> {

    @Query("delete from user_table")
    override suspend fun deleteAll()

    @Query("select * from user_table where user_name = :primaryKey")
    override suspend fun find(primaryKey: String) : Flow<User?>

    @Query("select * from user_table")
    override suspend fun getAll() : Flow<List<User>>

    @Query("select * from user_table")
    suspend fun getUserWithTasks() : Flow<List<UserWithTasks>>

    @Query("select * from user_table " +
            "where user_name = :name " +
            "and user_password = :password " +
            "and 1"
    ) suspend fun filter(
        name: String? = null,
        password: String? = null,
    ) : Flow<List<User>>
}