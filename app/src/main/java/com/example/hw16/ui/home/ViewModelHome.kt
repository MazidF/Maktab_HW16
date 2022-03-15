package com.example.hw16.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hw16.data.MyRepository
import com.example.hw16.model.TaskItemUiState
import com.example.hw16.model.TaskState.*
import com.example.hw16.utils.Mapper.toTaskItemUiState

class ViewModelHome(
    private val repository: MyRepository
) : ViewModel() {
    private var hasBeenLoaded = false
    val viewPool = RecyclerView.RecycledViewPool()
    val notifyState = MutableLiveData<NotifyDataChange>()

    init {
        repository.taskChangeState.observeForever {
            val (t, i) = it
            val position = array.indexOfFirst { task ->
                task.id == i
            }
            t?.let { task ->
                if (position == -1) {
                    addTask(task.toTaskItemUiState())
                } else {
                    array[position] = task.toTaskItemUiState()
                }
                return@observeForever
            }
            array.removeAt(position)
            removeTask(t!!.toTaskItemUiState(), false)
        }
    }

    private val array = ArrayList<TaskItemUiState>(5)
    private val _taskList = MutableLiveData(array)
    val taskList: LiveData<ArrayList<TaskItemUiState>> = _taskList

    val listDone by lazy {
        ArrayList<TaskItemUiState>(5)
    }

    val listDoing by lazy {
        ArrayList<TaskItemUiState>(5)
    }

    val listTodo by lazy {
        ArrayList<TaskItemUiState>(5)
    }

    fun getTasks(userName: String) {
        if (hasBeenLoaded) return
        hasBeenLoaded = true
        val rawList = repository.getUserTasks(userName)
        val list = rawList.map { task ->
            task.toTaskItemUiState().also {
                addTask(it, addToList = false, notify = false)
            }
        }
        array.addAll(list)
        notifyState.value = NotifyDataChange.Notify()
    }

    private fun addTask(task: TaskItemUiState, addToList: Boolean = true, notify: Boolean = true) {
        if (addToList) {
            array.add(task)
        }
        when (task.state) {
            DONE -> listDone.add(task)
            DOING -> listDoing.add(task)
            TODO -> listTodo.add(task)
        }
        if (notify)
            notifyState.value = NotifyDataChange.Notify(task.state)
    }

    private fun removeTask(
        task: TaskItemUiState,
        removeFromList: Boolean = true,
        notify: Boolean = true
    ) {
        if (removeFromList) {
            array.remove(task)
        }
        when (task.state) {
            DONE -> listDone.remove(task)
            DOING -> listDoing.remove(task)
            TODO -> listTodo.remove(task)
        }
        if (notify)
            notifyState.value = NotifyDataChange.Notify(task.state)
    }

    fun removeItems(vararg items: TaskItemUiState) {
        val listId = items.map { it.id }
        repository.removeTask(*listId.toLongArray())
        for (item in items) {
            removeTask(item)
        }
    }
}

