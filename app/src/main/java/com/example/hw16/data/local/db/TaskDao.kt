package com.example.hw16.data.local.db

import androidx.room.*
import com.example.hw16.model.Task

@Dao
interface TaskDao : MyDao<Task, Long> {

    @Query("delete from task_table")
    override fun deleteAll()

    @Query("select * from task_table where id = :primaryKey")
    override fun find(primaryKey: Long): Task?

    @Query("select * from task_table")
    override fun getAll(): List<Task>
}
