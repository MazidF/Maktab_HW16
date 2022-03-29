package com.example.hw16.data.local

import com.example.hw16.data.local.db.MyDao
import com.example.hw16.utils.logger
import kotlinx.coroutines.flow.Flow

abstract class LocalDataSource<Item, PrimaryKey>(private val dao: MyDao<Item, PrimaryKey>) {

    suspend fun insert(vararg items: Item) : List<Long> {
        return dao.insertItem(*items)
    }

    fun get(): Flow<List<Item>> {
        return dao.getAll()
    }

    suspend fun update(vararg items: Item) {
        val result = dao.updateItem(*items)
        logger("result of update : $result")
    }

    suspend fun remove(vararg items: Item, removeAll: Boolean) {
        if (removeAll) {
            dao.deleteAll()
        } else {
            dao.deleteItem(*items)
        }
    }

    fun find(primaryKey: PrimaryKey): Flow<Item?> {
        return dao.find(primaryKey)
    }
}
