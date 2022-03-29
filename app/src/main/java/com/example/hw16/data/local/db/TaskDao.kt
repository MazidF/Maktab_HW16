package com.example.hw16.data.local.db

import androidx.room.Dao
import androidx.room.Query
import com.example.hw16.model.Task
import com.example.hw16.model.TaskWithSubTask
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao : MyDao<Task, Long> {

    @Query("delete from task_table")
    override suspend fun deleteAll()

    @Query("select * from task_table where task_id = :primaryKey")
    override fun find(primaryKey: Long): Flow<Task?>

    @Query("select * from task_table where task_id = :primaryKey")
    fun findTaskWithSubTasks(primaryKey: Long): Flow<TaskWithSubTask?>

    @Query("select * from task_table order by task_deadline asc")
    override fun getAll(): Flow<List<Task>>

    @Query("select * from task_table where user_owner_id = :username order by task_is_done, task_deadline asc")
    fun getUserTasks(username: String) : Flow<List<Task>>

    @Query("select * from task_table where user_owner_id = :username order by task_is_done, task_deadline asc")
    fun getUserTasksWithSubTasks(username: String) : Flow<List<TaskWithSubTask>>

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
    ) : Flow<List<Task>>

    @Query("delete from task_table where task_id in (:ids)")
    suspend fun deleteItem(vararg ids: Long): Int

    @Query("select * from task_table")
    fun taskWithSubTasks(): Flow<List<TaskWithSubTask>>


}
