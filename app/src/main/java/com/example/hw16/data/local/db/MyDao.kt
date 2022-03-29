package com.example.hw16.data.local.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

interface MyDao<Item, PrimaryKey> {

/*    @RawQuery
    fun rawQuery(query: SimpleSQLiteQuery)*/

    fun getAll() : Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(vararg items: Item): List<Long>

    @Delete
    suspend fun deleteItem(vararg items: Item)

    @Update
    suspend fun updateItem(vararg item: Item): Int

    fun find(primaryKey: PrimaryKey): Flow<Item?>

    suspend fun deleteAll()
}
