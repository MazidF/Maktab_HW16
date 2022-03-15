package com.example.hw16.data.local

import com.example.hw16.data.local.db.MyDao

abstract class LocalDataSource<Item, PrimaryKey>(private val dao: MyDao<Item, PrimaryKey>) {

    fun insert(vararg items: Item) : List<Long> {
        return dao.insertItem(*items)
    }

    fun get(): List<Item> {
        return dao.getAll()
    }

    fun update(vararg items: Item) {
        dao.updateItem(*items)
    }

    fun remove(vararg items: Item, removeAll: Boolean) {
        if (removeAll) {
            dao.deleteAll()
        } else {
            dao.deleteItem(*items)
        }
    }

    fun find(primaryKey: PrimaryKey): Item? {
        return dao.find(primaryKey)
    }
}
