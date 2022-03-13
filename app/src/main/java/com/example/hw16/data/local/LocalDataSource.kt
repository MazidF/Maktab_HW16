package com.example.hw16.data.local

interface LocalDataSource<Item, PrimaryKey> {

    fun save(vararg item: Item, applyChanges: Boolean = true)

    fun load() : List<Item>

    fun update(item: Item)

    fun remove(vararg items: Item, removeAll: Boolean = false)

}
