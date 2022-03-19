package com.example.hw16.data.local

import com.example.hw16.data.local.db.MyDao
import kotlinx.coroutines.flow.Flow

abstract class LocalDataSource<Item, PrimaryKey>(private val dao: MyDao<Item, PrimaryKey>) {

    suspend fun insert(vararg items: Item) : List<Long> {
        return dao.insertItem(*items)
    }

    suspend fun get(): Flow<List<Item>> {
        return dao.getAll()
    }

    suspend fun update(vararg items: Item) {
        dao.updateItem(*items)
    }

    suspend fun remove(vararg items: Item, removeAll: Boolean) {
        if (removeAll) {
            dao.deleteAll()
        } else {
            dao.deleteItem(*items)
        }
    }

    suspend fun find(primaryKey: PrimaryKey): Flow<Item?> {
        return dao.find(primaryKey)
    }
}
