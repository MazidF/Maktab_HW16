package com.example.hw16.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.hw16.data.local.*
import com.example.hw16.model.SubTask
import com.example.hw16.model.Task
import com.example.hw16.model.TaskWithSubTask
import com.example.hw16.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.Serializable

class MyRepository(
    private val userDataSource: UserDataSource,
    private val taskDataSource: TaskDataSource,
    private val subTaskDataSource: SubTaskDataSource,
    private val fileDataSource: FileLocalDataSource
) {

    suspend fun removeTask(vararg ids: Long) {
        taskDataSource.removeWithId(*ids)
    }

    suspend fun addTask(task: Task): Long {
        return taskDataSource.insert(task)[0]
    }

    suspend fun editTask(task: Task) {
        taskDataSource.update(task)
    }

    suspend fun removeSubTask(vararg ids: Long) {
        subTaskDataSource.removeWithId(*ids)
    }

    suspend fun addSubTask(subTask: SubTask): Long {
        return subTaskDataSource.insert(subTask)[0]
    }

    suspend fun editSubTask(vararg subTask: SubTask) {
        subTaskDataSource.update(*subTask)
    }

    fun searchTasks(
        title: String? = null,
        description: String? = null,
        deadline: Long? = null,
        isDone: Boolean? = null,
        imageUri: String? = null,
    ): Flow<List<Task>> {
        return taskDataSource.search(title, description, deadline, isDone, imageUri)
    }

    fun getTask(id: Long): Flow<Task?> {
        return taskDataSource.find(id)
    }

    fun getUserTasks(username: String): Flow<List<Task>> {
        return taskDataSource.getUserTasks(username)
    }

    fun getTaskWithSubTasks(taskId: Long): LiveData<TaskWithSubTask?> {
        return taskDataSource.getTaskWithSubTasks(taskId).asLiveData()
    }

    suspend fun signInUser(user: User): LiveData<User?> {
        return try {
            userDataSource.insert(user)
            userDataSource.find(user.username).asLiveData()
        } catch (e: Exception) {
            MutableLiveData(null)
        }
    }

    fun logInUser(userName: String, password: String): LiveData<Pair<Boolean, User?>> {
        return userDataSource.find(userName).asLiveData().map { user ->
            if (user == null) {
                Pair(false, null)
            } else {
                Pair(password == user.password, user)
            }
        }
    }

    //////////////////////// file //////////////////////

    fun getRootPath(
        context: Context,
        fileType: FileType
    ) = fileDataSource.getRoot(context, fileType).absolutePath

    fun getFilePath(
        context: Context,
        fileType: FileType,
        fileName: String
    ) = fileDataSource.getFile(context, fileType, fileName)?.absolutePath

    fun save(
        context: Context,
        fileType: FileType,
        fileName: String,
        byteArray: ByteArray,
        override: Boolean = false,
        waitUntilFileIsReady: Boolean = false
    ) : String? {
        return fileDataSource.save(context, fileType, fileName, byteArray, override, waitUntilFileIsReady)
    }

    fun load(
        context: Context,
        fileType: FileType,
        fileName: String
    ) {
        fileDataSource.load(context, fileType, fileName)
    }

    fun <T : Serializable> saveObject(file: File, t: T) {
        fileDataSource.writeObject(file, t)
    }

    fun <T : Serializable> saveObject(file: File): T? {
        return fileDataSource.readObject(file)
    }

    fun removeFile(uri: String) {
        fileDataSource.deleteFile(uri)
    }

    fun getSubTasksOfTask(taskId: Long): Flow<List<SubTask>> {
        return subTaskDataSource.getSubTasksOfTask(taskId)
    }
}
