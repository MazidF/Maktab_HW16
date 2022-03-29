package com.example.hw16.data.local

import com.example.hw16.data.local.db.SubTaskDao
import com.example.hw16.model.SubTask
import kotlinx.coroutines.flow.Flow

class SubTaskDataSource(
    private val subTaskDao: SubTaskDao
) : LocalDataSource<SubTask, Long>(subTaskDao) {

    suspend fun removeWithId(vararg ids: Long) {
        subTaskDao.deleteItem(*ids)
    }

    fun getSubTasksOfTask(taskId: Long): Flow<List<SubTask>> {
        return subTaskDao.getTaskSubTasks(taskId)
    }
}