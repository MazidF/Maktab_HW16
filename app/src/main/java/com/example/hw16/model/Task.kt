package com.example.hw16.model

import androidx.room.*

@Entity(tableName = Task.TABLE_NAME)
data class Task(
    @ColumnInfo(name = "user_owner_id") val userName: String,
    @ColumnInfo(name = "task_title") val title: String,
    @ColumnInfo(name = "task_description") val description: String,
    @ColumnInfo(name = "task_deadline") val deadline: Long,
    @ColumnInfo(name = "task_image_uri") val image_uri: String? = null,
    @ColumnInfo(name = "task_is_done") val isDone: Boolean = false,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_id") val id: Long = 0
) {
    companion object {
        const val TABLE_NAME = "task_table"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
