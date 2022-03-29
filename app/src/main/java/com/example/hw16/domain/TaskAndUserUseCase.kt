package com.example.hw16.domain

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.hw16.data.MyRepository
import com.example.hw16.data.local.FileType
import com.example.hw16.model.*
import com.example.hw16.ui.ProgressResult
import com.example.hw16.utils.Mapper.toTaskItemUiState
import com.example.hw16.utils.logger
import com.example.hw16.utils.observeForever
import java.io.ByteArrayOutputStream
import java.io.File

class TaskAndUserUseCase(
    private val repository: MyRepository,
    private val rootFile: File
) {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun logout() {
        _user.value = null
    }

    fun login(userName: String, password: String): LiveData<ProgressResult> {
        val result = MutableLiveData<ProgressResult>()
        val liveData = repository.logInUser(userName, password)
        observeForever(liveData) {
            if (it.first) {
                it.second?.let { user ->
                    _user.value = user
                    result.value = ProgressResult.SUCCESS
                    return@observeForever
                }
            }
            result.value = ProgressResult.FAIL
        }
        return result
    }

    suspend fun signIn(user: User): LiveData<ProgressResult> {
        logger("sign_in")
        val result = MutableLiveData<ProgressResult>()
        observeForever(repository.signInUser(user)) {
            if (it != null) {
                _user.value = it
                result.value = ProgressResult.SUCCESS
            } else {
                result.value = ProgressResult.FAIL
            }
        }
        return result
    }

    fun saveImageToFile(context: Context, fileType: FileType, bitmap: Bitmap): String? {
        val output = ByteArrayOutputStream()
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)) {
            return null
        }
        val fileName = System.currentTimeMillis().toString() + fileType.format
        return repository.save(
            context,
            fileType,
            fileName,
            output.toByteArray(),
            waitUntilFileIsReady = true
        )
    }

    fun removeFile(uri: String) {
        repository.removeFile(uri)
    }

    //////////////////////////////////////////////////////////

    private var taskList = ArrayList<TaskItemUiState>()
    private val _tasks = MutableLiveData<ArrayList<TaskItemUiState>>()
    val tasks: LiveData<ArrayList<TaskItemUiState>> = _tasks

    val listDone = MutableLiveData<ArrayList<TaskItemUiState>>(ArrayList(5))
    val listDoing = MutableLiveData<ArrayList<TaskItemUiState>>(ArrayList(5))
    val listTodo = MutableLiveData<ArrayList<TaskItemUiState>>(ArrayList(5))

    fun getTasks() {
        val user = user.value ?: run {
            logger("user was null so end getTasks")
            return
        }
        val liveData = repository.getUserTasks(user.username).asLiveData()
        observeForever(liveData) { list ->
            clearLists()
            val resultList = list.map { task ->
                task.toTaskItemUiState()
                    /*.also { taskItemUiState ->
                        separate(taskItemUiState)
                    }*/
            }
            val now = System.currentTimeMillis()
            val firstDoing = resultList.indexOfFirst {
                it.deadline.time > now
            }
            val firstDone = resultList.indexOfFirst {
                it.isDone()
            }
            if (firstDoing != -1) {
                listTodo.value = ArrayList(resultList.subList(0, firstDoing))
                if (firstDone != -1) {
                    listDoing.value = ArrayList(resultList.subList(firstDoing, firstDone))
                    listDone.value = ArrayList(resultList.subList(firstDone, resultList.size))
                } else {
                    listDoing.value = ArrayList(resultList.subList(firstDoing, resultList.size))
                    listDone.value = ArrayList()
                }
            } else {
                listDoing.value = ArrayList()
                if (firstDone != -1) {
                    listTodo.value = ArrayList(resultList.subList(0, firstDone))
                    listDone.value = ArrayList()
                } else {
                    listTodo.value = ArrayList(resultList.subList(0, resultList.size))
                    listDone.value = ArrayList()
                }
            }
            taskList = ArrayList(resultList)
            _tasks.value = taskList
        }
    }

    private fun clearLists() {
        listDone.value!!.clear()
        listDoing.value!!.clear()
        listTodo.value!!.clear()
    }

    private fun notifyChanges() {
        _tasks.value = taskList
        listDone.value = listDone.value
        listDoing.value = listDoing.value
        listTodo.value = listTodo.value
    }

    private fun updateList() {
        getTasks()
    }

    suspend fun addTask(task: Task) {
        repository.addTask(task)
        updateList()
    }

    suspend fun removeTask(task: TaskItemUiState) {
        repository.removeTask(task.id)
        updateList()
    }

    suspend fun editTask(task: Task) {
        repository.editTask(task)
        updateList()
    }

    suspend fun addSubTask(subTask: SubTask) {
        repository.addSubTask(subTask)
    }

    suspend fun removeSubTask(subTask: SubTaskItemUiState) {
        repository.removeSubTask(subTask.id)
    }

    suspend fun editSubTask(subTask: SubTask) {
        repository.editSubTask(subTask)
    }

    private fun separate(taskItemUiState: TaskItemUiState) {
        when (taskItemUiState.state) {
            TaskState.DONE -> listDone.value!!.add(taskItemUiState)
            TaskState.DOING -> listDoing.value!!.add(taskItemUiState)
            TaskState.TODO -> listTodo.value!!.add(taskItemUiState)
        }
    }

    fun getTaskOfDay(from: Long, to: Long): LiveData<List<Task>> {
        // TODO: Implement
        return MutableLiveData()
    }

    fun getTaskWithSubTasks(taskId: Long): LiveData<TaskWithSubTask?> {
        return repository.getTaskWithSubTasks(taskId)
    }

    fun getSubTasksOfTask(taskId: Long): LiveData<List<SubTask>> {
        return repository.getSubTasksOfTask(taskId).asLiveData()
    }
}
