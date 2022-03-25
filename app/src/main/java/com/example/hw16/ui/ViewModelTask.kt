package com.example.hw16.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw16.data.MyRepository
import com.example.hw16.model.TaskItemUiState
import java.io.File

class ViewModelTask(
    private val repository: MyRepository,
    private val rootFile: File
) : ViewModel() {

    private val taskList = ArrayList<TaskItemUiState>()
    val tasks by lazy {
        MutableLiveData<List<TaskItemUiState>>(taskList)
    }

    val listDone by lazy {
        ArrayList<Int>(5)
    }

    val listDoing by lazy {
        ArrayList<Int>(5)
    }

    val listTodo by lazy {
        ArrayList<Int>(5)
    }

    fun getTasks(user: String) {

    }
}