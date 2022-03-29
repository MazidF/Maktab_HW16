package com.example.hw16.utils

import com.example.hw16.model.SubTask
import com.example.hw16.model.SubTaskItemUiState
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
            state = getState()
        )
    }

    fun TaskItemUiState.toTask(username: String, isDone: Boolean = isDone()): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            deadline = deadline,
            image_uri = image_uri,
            userName = username,
            isDone = isDone
        )
    }

    private fun getImageName(imageUri: String): String {
        val index = imageUri.lastIndexOf('/')
        return imageUri.substring(index + 1)
    }

    fun SubTask.toSubTaskItemUiState() : SubTaskItemUiState {
        return SubTaskItemUiState(
            id = id,
            title = title,
            isDone = isDone,
            position = position
        )
    }

    fun SubTaskItemUiState.toSubTask(taskOwnerId: Long) : SubTask {
        return SubTask(
            id = id,
            title = title,
            isDone = isDone,
            position = position,
            ownerId = taskOwnerId
        )
    }
}
