package com.example.hw16.data.local

import com.example.hw16.data.local.db.MyDao
import com.example.hw16.model.User

class UserDataSource(
    private val userDao: MyDao<User, String>
) : LocalDataSource<User, String> {

    override fun save(vararg item: User, applyChanges: Boolean) {
        if (applyChanges) {
            userDao.updateItem(*item)
        } else {
            userDao.insertItem(*item)
        }
    }

    override fun load(): List<User> {
        return userDao.getAll()
    }

    override fun update(item: User) {
        userDao.updateItem(item)
    }

    override fun remove(vararg items: User, removeAll: Boolean) {
        if (removeAll) {
            userDao.deleteAll()
        } else {
            userDao.deleteItem(*items)
        }
    }
}

