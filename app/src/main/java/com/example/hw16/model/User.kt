package com.example.hw16.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = User.TABLE_NAME)
data class User(
    @ColumnInfo(name = "user_name") val username: String,
    @ColumnInfo(name = "user_password") val password: String,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val id: Long = 0
) {
    companion object {
        const val TABLE_NAME = "user_table"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
