package com.example.hw16.data.local.db

import androidx.room.*

interface MyDao<Item, PrimaryKey> {

    fun getAll() : List<Item>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(vararg items: Item)

    @Delete
    fun deleteItem(vararg items: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(vararg item: Item)

    fun find(primaryKey: PrimaryKey): Item?

    fun deleteAll()
}
