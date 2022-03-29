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
    private val _tasks = MutableLiveData<List<TaskItemUiState>>()
    val tasks: LiveData<List<TaskItemUiState>> = _tasks

    val listDone by lazy {
        ArrayList<Int>(5)
    }

    val listDoing by lazy {
        ArrayList<Int>(5)
    }

    val listTodo by lazy {
        ArrayList<Int>(5)
    }

    fun getTasks(fileType: FileType = FileType.IMAGE_FILE) {
        val user = user.value ?: run {
            logger("user was null so end getTasks")
            return
        }
        val liveData = repository.getUserTasks(user.username).asLiveData()
        val rootPath = rootFile.absolutePath + File.separator + fileType.value + File.separator
        clearLists()
        observeForever(liveData) { list ->
            val resultList = list.mapIndexed { index, task ->
                task.toTaskItemUiState(/*if (task.image_uri == "") "" else rootPath + task.image_uri*/)
                    .also { taskItemUiState ->
                        separate(taskItemUiState, index)
                    }
            }
            taskList = ArrayList(resultList)
            _tasks.value = taskList
        }
    }

    private fun clearLists() {
        listDone.clear()
        listDoing.clear()
        listTodo.clear()
    }

    private fun updateList() {
//        clearLists()
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

    private fun separate(taskItemUiState: TaskItemUiState, index: Int) {
        when (taskItemUiState.state) {
            TaskState.DONE -> listDone.add(index)
            TaskState.DOING -> listDoing.add(index)
            TaskState.TODO -> listTodo.add(index)
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
