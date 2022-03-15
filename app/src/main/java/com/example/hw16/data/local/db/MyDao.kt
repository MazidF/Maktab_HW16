package com.example.hw16.data.local.db

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery

interface MyDao<Item, PrimaryKey> {

/*    @RawQuery
    fun rawQuery(query: SimpleSQLiteQuery)*/

    fun getAll() : List<Item>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(vararg items: Item): List<Long>

    @Delete
    fun deleteItem(vararg items: Item)

    @Update
    fun updateItem(vararg item: Item)

    fun find(primaryKey: PrimaryKey): Item?

    fun deleteAll()
}
