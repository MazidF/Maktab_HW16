package com.example.hw16.data.local.db

import androidx.room.*
import com.example.hw16.model.Task


@Dao
interface TaskDao : MyDao<Task, Long> {

    @Query("delete from task_table")
    override fun deleteAll()

    @Query("select * from task_table where task_id = :primaryKey")
    override fun find(primaryKey: Long): Task?

    @Query("select * from task_table order by task_deadline asc")
    override fun getAll(): List<Task>

    @Query("select * from task_table where user_owner_id = :username")
    fun getUserTasks(username: String) : List<Task>

    @Query("select * from task_table " +
            "where task_deadline = :deadline " +
            "and task_description = :description " +
            "and task_title = :title " +
            "and task_is_done = :isDone " +
            "and task_image_uri = :imageUri " +
            "and task_deadline > :after " +
            "and task_deadline < :before " +
            "and 1"
    ) fun filter(
        title: String? = null,
        description: String? = null,
        deadline: Long? = null,
        isDone: Boolean? = null,
        imageUri: String? = null,
        after: Long? = null,
        before: String? = null,
    ) : List<Task>

    @Query("delete from task_table where task_id in (:ids)")
    fun deleteItem(vararg ids: Long)
}
