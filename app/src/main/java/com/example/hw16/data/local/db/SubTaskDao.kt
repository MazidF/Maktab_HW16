package com.example.hw16.data.local.db

import androidx.room.Dao
import androidx.room.Query
import com.example.hw16.model.SubTask
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao : MyDao<SubTask, Long> {

    @Query("delete from sub_task_table")
    override suspend fun deleteAll()

    @Query("select * from sub_task_table")
    override fun getAll(): Flow<List<SubTask>>

    @Query("select * from sub_task_table where sub_task_id = :primaryKey")
    override fun find(primaryKey: Long): Flow<SubTask?>

    @Query("select * from sub_task_table where sub_task_owner_task_id = :taskId")
    fun getTaskSubTasks(taskId: Long): Flow<List<SubTask>>

    @Query("delete from sub_task_table where sub_task_id in (:ids)")
    suspend fun deleteItem(vararg ids: Long)
}
