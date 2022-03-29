package com.example.hw16.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw16.domain.TaskAndUserUseCase
import com.example.hw16.model.SubTask
import com.example.hw16.model.SubTaskItemUiState
import com.example.hw16.model.Task
import com.example.hw16.model.TaskWithSubTask
import com.example.hw16.utils.observeForever
import kotlinx.coroutines.launch

class ViewModelTask(
    private val useCase: TaskAndUserUseCase
) : ViewModel() {
    private val _subTasks = MutableLiveData<List<SubTask>>()
    val subTasks: LiveData<List<SubTask>> = _subTasks

    fun getTaskWithSubTasks(taskId: Long) : LiveData<TaskWithSubTask?> {
        return useCase.getTaskWithSubTasks(taskId)
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            useCase.editTask(task)
        }
    }

    fun addSubTask(subTask: SubTask) {
        viewModelScope.launch {
            useCase.addSubTask(subTask)
            getSubTasks(subTask.ownerId)
        }
    }

    fun editSubTask(subTask: SubTask) {
        viewModelScope.launch {
            useCase.editSubTask(subTask)
            getSubTasks(subTask.ownerId)
        }
    }

    fun removeSubTask(ownerId: Long, subTask: SubTaskItemUiState) {
        viewModelScope.launch {
            useCase.removeSubTask(subTask)
            getSubTasks(ownerId)
        }
    }

    private fun getSubTasks(ownerId: Long) {
        observeForever(useCase.getSubTasksOfTask(ownerId)) {
            _subTasks.value = it
        }
    }
}
