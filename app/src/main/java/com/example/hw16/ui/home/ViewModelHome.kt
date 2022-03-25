package com.example.hw16.ui.home

import android.content.Context
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.hw16.data.MyRepository
import com.example.hw16.data.local.FileType
import com.example.hw16.model.Task
import com.example.hw16.model.TaskItemUiState
import com.example.hw16.model.TaskState.*
import com.example.hw16.model.User
import com.example.hw16.utils.Mapper.toTaskItemUiState
import com.example.hw16.utils.observeForever
import kotlinx.coroutines.launch
import java.io.File

class ViewModelHome(
    private val repository: MyRepository
) : ViewModel() {
    var user: User? = null
    private var hasBeenLoaded = false
    private lateinit var rootImage: String
    val viewPool = RecyclerView.RecycledViewPool()
    val notifyState = MutableLiveData<NotifyDataChange>()

    init {
        with(repository) {
//            userChangeState.observeForever {
//                if (it != null) {
//                    this@ViewModelHome.user = it
//                } else {
//                    resetLists()
//                }
//            }
//            taskChangeState.observeForever {
//                if (it == true) {
//                    updateTasks()
//                }
//            }
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

    private fun updateTasks() {
        resetLists(false)
        user?.let {
            loadTasks(userName = it.username, root = rootImage)
        }
    }

    private fun resetLists(notify: Boolean = true) {
        listDone.clear()
        listDoing.clear()
        listTodo.clear()
        taskList.value!!.clear()
        hasBeenLoaded = false
        if (notify) {
            notifyState.postValue(NotifyDataChange.Notify())
        }
    }

    fun getTasks(context: Context) {
        user?.let {
            loadTasks(context, it.username)
        }
    }

    private fun loadTasks(context: Context? = null, userName: String, root: String? = null) {
        if (hasBeenLoaded) return
        hasBeenLoaded = true
        rootImage = context?.run { repository.getRootPath(this, FileType.IMAGE_FILE) } ?: root!!
        val liveData = repository.getUserTasks(userName).asLiveData()
        val rootPath = rootImage + File.separator
        observeForever(liveData) { rawList ->
            var prefix: String
            val list = rawList.map { task ->
                prefix = if(task.image_uri == "") "" else rootPath
                task.toTaskItemUiState(prefix).also {
                    addTask(it, addToList = false, notify = false)
                }
            }
            array.addAll(list)
            notifyState.value = NotifyDataChange.Notify()
        }
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
        viewModelScope.launch {
            repository.removeTask(*listId.toLongArray())
        }
        for (item in items) {
            removeTask(item)
        }
    }
}

