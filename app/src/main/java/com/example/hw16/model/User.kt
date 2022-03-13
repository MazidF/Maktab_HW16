package com.example.hw16.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "password") val password: String
)
