package com.example.hw16.model

data class TaskItemUiState(
    val title: String,
    val description: String,
    val deadline: Long,
    val image_uri: String,
    val id: Long,
    val state: TaskState
) {
    fun getTime() = com.example.hw16.utils.getTime(deadline)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskItemUiState

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
