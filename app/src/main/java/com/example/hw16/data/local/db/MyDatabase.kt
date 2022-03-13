package com.example.hw16.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hw16.model.Task
import com.example.hw16.model.User

@Database(entities = [User::class, Task::class], version = 1, exportSchema = true)
abstract class MyDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getTaskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE != null) {
                    return@synchronized INSTANCE!!
                }
                val instance = Room.databaseBuilder(
                    context,
                    MyDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}