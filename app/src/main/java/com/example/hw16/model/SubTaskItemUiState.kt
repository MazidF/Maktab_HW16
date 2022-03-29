package com.example.hw16.model


data class SubTaskItemUiState(
    val id: Long,
    var title: String,
    var isDone: Boolean,
    var position: Int
    // TODO: change var to val
) {
    companion object {
        fun empty(position: Int) = SubTaskItemUiState(
            id = -1,
            title = "",
            isDone = false,
            position = position
        )
    }
}