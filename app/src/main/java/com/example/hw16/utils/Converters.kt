package com.example.hw16.utils

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun Date.toLong() = time

    @TypeConverter
    fun Long.toDate() = Date(this)
}
