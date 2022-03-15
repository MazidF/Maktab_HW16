package com.example.hw16.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithTasks(
    @Embedded val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_owner_id"
    ) val  tasks: List<Task>
)
