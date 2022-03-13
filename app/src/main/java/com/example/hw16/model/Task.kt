package com.example.hw16.model

import androidx.room.*

@Entity(tableName = "task_table")
data class Task(
     @ColumnInfo(name = "task_title") val title: String,
     @ColumnInfo(name = "task_description") val description: String,
     @ColumnInfo(name = "task_deadline") val deadline: Long,
     @ColumnInfo(name = "task_image_uri") val image_uri: String = "",
     @PrimaryKey(autoGenerate = true) val id: Long? = null,
     ) {
     @Ignore lateinit var state: TaskState
}
