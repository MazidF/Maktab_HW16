package com.example.hw16.data.local

import com.example.hw16.data.local.db.MyDao
import com.example.hw16.model.Task

class TaskDataSource(
    private val taskDao: MyDao<Task, Long>
) : LocalDataSource<Task, Long> {

    override fun save(vararg item: Task, applyChanges: Boolean) {
        if (applyChanges) {
            taskDao.updateItem(*item)
        } else {
            taskDao.insertItem(*item)
        }
    }

    override fun load(): List<Task> {
        return taskDao.getAll()
    }

    override fun update(item: Task) {
        taskDao.updateItem(item)
    }

    override fun remove(vararg items: Task, removeAll: Boolean) {
        if (removeAll) {
            taskDao.deleteAll()
        } else {
            taskDao.deleteItem(*items)
        }
    }
}