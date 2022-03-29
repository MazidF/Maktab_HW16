package com.example.hw16.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw16.domain.TaskAndUserUseCase
import com.example.hw16.model.Task
import com.example.hw16.model.TaskItemUiState
import kotlinx.coroutines.launch

class ViewModelHome(
    private val useCase: TaskAndUserUseCase
) : ViewModel() {
    val user = useCase.user
    var tasks: LiveData<ArrayList<TaskItemUiState>> = useCase.tasks
    val listDone: LiveData<ArrayList<TaskItemUiState>> = useCase.listDone
    val listDoing: LiveData<ArrayList<TaskItemUiState>> = useCase.listDoing
    val listTodo: LiveData<ArrayList<TaskItemUiState>> = useCase.listTodo
//    val viewPool = RecyclerView.RecycledViewPool()

    init {
        with(useCase) {
            user.observeForever {
                if (it != null) {
                    getTasks()
                }
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

