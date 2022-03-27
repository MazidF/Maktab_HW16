package com.example.hw16.data.local.db

import androidx.room.Query
import com.example.hw16.model.SubTask
import kotlinx.coroutines.flow.Flow

interface SubTaskDao : MyDao<SubTask, Long> {

    @Query("select * from sub_task_table")
    override fun getAll(): Flow<List<SubTask>>

    @Query("select * from sub_task_table where sub_task_owner_task_id = :taskId")
    fun getSubTasks(taskId: Long)
}
