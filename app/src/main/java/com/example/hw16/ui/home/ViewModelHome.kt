package com.example.hw16.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.hw16.domain.TaskAndUserUseCase
import com.example.hw16.model.Task
import com.example.hw16.model.TaskItemUiState
import kotlinx.coroutines.launch

class ViewModelHome(
    private val useCase: TaskAndUserUseCase
) : ViewModel() {
    val user = useCase.user
    var tasks = useCase.tasks
    val listDone = useCase.listDone
    val listDoing = useCase.listDoing
    val listTodo = useCase.listTodo
    val viewPool = RecyclerView.RecycledViewPool()

    init {
        with(useCase) {
            user.observeForever {
                getTasks()
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            useCase.addTask(task)
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            useCase.editTask(task)
        }
    }

    fun removeTask(task: TaskItemUiState) {
        viewModelScope.launch {
            useCase.removeTask(task)
        }
    }

    fun removeImage(imageUri: String) {
        useCase.removeFile(imageUri)
    }
}

