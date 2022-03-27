package com.example.hw16.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = SubTask.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["task_id"],
            childColumns = ["sub_task_owner_task_id"]
        )
    ]
)
data class SubTask(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sub_task_id") val id: Long = 0,
    @ColumnInfo(name = "sub_task_owner_task_id") val ownerId: Int,
    @ColumnInfo(name = "sub_task_title") val title: String,
    @ColumnInfo(name = "sub_task_is_done") val isDone: Boolean,
    @ColumnInfo(name = "sub_task_number") val number: Int
) {
    companion object {
        const val TABLE_NAME = "sub_task_table"
    }
}