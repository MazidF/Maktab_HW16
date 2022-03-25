package com.example.hw16.utils

import com.example.hw16.model.Task
import com.example.hw16.model.TaskItemUiState

object Mapper {

    fun Task.toTaskItemUiState(prefix: String? = null): TaskItemUiState {
        return TaskItemUiState(
            title = title,
            description = description,
            deadline = getTime(deadline),
            image_uri = prefix ?: "" + image_uri,
            id = id,
            state = getState(isDone, deadline)
        )
    }

}