package com.example.hw16.model

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithSubTask(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "sub_task_owner_task_id"
    ) val subTasks: List<SubTask>
)
