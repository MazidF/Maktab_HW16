package com.example.hw16.data.local

import com.example.hw16.data.local.db.TaskDao
import com.example.hw16.model.Task
import com.example.hw16.model.TaskWithSubTask
import com.example.hw16.utils.logger
import kotlinx.coroutines.flow.Flow

class TaskDataSource(
    private val taskDao: TaskDao
) : LocalDataSource<Task, Long>(taskDao) {

    fun getUserTasks(username: String): Flow<List<Task>> {
        return taskDao.getUserTasks(username)
    }

    suspend fun getTaskAfter() {

    }

    fun search(
        title: String? = null,
        description: String? = null,
        deadline: Long? = null,
        isDone: Boolean? = null,
        imageUri: String? = null,
        after: Long? = null,
        before: String? = null,
    ) : Flow<List<Task>> {
        return taskDao.filter(title, description, deadline, isDone, imageUri, after, before)
    }

    suspend fun removeWithId(vararg ids: Long) {
        val result = taskDao.deleteItem(*ids)
        logger("result of delete: $result")
    }

    fun getTaskWithSubTasks(primaryKey: Long): Flow<TaskWithSubTask?> {
        return taskDao.findTaskWithSubTasks(primaryKey)
    }
}