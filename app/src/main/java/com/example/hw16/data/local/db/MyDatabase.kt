package com.example.hw16.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hw16.model.SubTask
import com.example.hw16.model.Task
import com.example.hw16.model.User
import com.example.hw16.utils.Converters

@Database(
    entities = [User::class, Task::class, SubTask::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getTaskDao(): TaskDao
    abstract fun getSubTaskDao(): SubTaskDao

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