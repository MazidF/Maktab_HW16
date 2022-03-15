package com.example.hw16.data.local

import com.example.hw16.data.local.db.MyDao
import com.example.hw16.data.local.db.TaskDao
import com.example.hw16.model.Task

class TaskDataSource(
    private val taskDao: TaskDao
) : LocalDataSource<Task, Long>(taskDao) {

    fun getUserTasks(username: String): List<Task> {
        return taskDao.getUserTasks(username)
    }

    fun getTaskAfter() {

    }

    fun search(
        title: String? = null,
        description: String? = null,
        deadline: Long? = null,
        isDone: Boolean? = null,
        imageUri: String? = null,
        after: Long? = null,
        before: String? = null,
    ) : List<Task> {
        return taskDao.filter(title, description, deadline, isDone, imageUri, after, before)
    }

    fun removeWithId(vararg ids: Long) {
        taskDao.deleteItem(*ids)
    }
}