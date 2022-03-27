package com.example.hw16.utils

import com.example.hw16.model.Task
import com.example.hw16.model.TaskItemUiState

object Mapper {

    fun Task.toTaskItemUiState(prefix: String? = null): TaskItemUiState {
        return TaskItemUiState(
            title = title,
            description = description,
            deadline = deadline,
            image_uri = prefix ?: "" + image_uri,
            id = id,
            state = getState(isDone, deadline)
        )
    }

    fun TaskItemUiState.toTask(username: String): Task {
        return Task(
            title = title,
            description = description,
            deadline = deadline,
            image_uri = getImageName(image_uri),
            userName = username
        )
    }

    private fun getImageName(imageUri: String): String {
        val index = imageUri.lastIndexOf('/')
        return imageUri.substring(index + 1)
    }

}