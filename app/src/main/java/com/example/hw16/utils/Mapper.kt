package com.example.hw16.utils

import com.example.hw16.model.Task
import com.example.hw16.model.TaskItemUiState

object Mapper {

    fun Task.toTaskItemUiState(): TaskItemUiState {
        return TaskItemUiState(
            title = title,
            description = description,
            deadline = getTime(deadline),
            image_uri = image_uri,
            id = id ?: throw Exception("Task Doesn't have ID!!!"),
            state = getState(isDone, deadline)
        )
    }

}