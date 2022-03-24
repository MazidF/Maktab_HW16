package com.example.hw16.data

import android.content.Context
import androidx.lifecycle.*
import com.example.hw16.data.local.FileLocalDataSource
import com.example.hw16.data.local.FileType
import com.example.hw16.data.local.TaskDataSource
import com.example.hw16.data.local.UserDataSource
import com.example.hw16.model.Task
import com.example.hw16.model.User
import com.example.hw16.utils.observeForever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.Serializable
import java.lang.Exception
import java.util.concurrent.ExecutorService

class MyRepository(
    private val userDataSource: UserDataSource,
    private val taskDataSource: TaskDataSource,
    private val fileDataSource: FileLocalDataSource
) {
    val taskChangeState = MutableLiveData<Boolean>()
    val userChangeState = MutableLiveData<User>()

    private val userUpdateObserver = Observer<User?> {
        userChangeState.postValue(it)
    }

    suspend fun removeTask(vararg ids: Long) {
        taskDataSource.removeWithId(*ids)
        taskChangeState.postValue(true)
    }

    suspend fun addTask(task: Task) {
        taskDataSource.insert(task)[0]
        taskChangeState.postValue(true)
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

    suspend fun editTask(task: Task) {
        taskDataSource.update(task)
        taskChangeState.postValue(true)
    }

    suspend fun signInUser(user: User): LiveData<User?> {
        return try {
            userDataSource.insert(user)
            userDataSource.find(user.username).asLiveData().also { liveData ->
                observeForever(liveData, userUpdateObserver)
            }
        } catch (e: Exception) {
            MutableLiveData(null)
        }
    }

    fun logInUser(userName: String, password: String): LiveData<Pair<Boolean, User?>> {
        return userDataSource.find(userName).asLiveData().also { liveData ->
            observeForever(liveData, userUpdateObserver)
            }.map { user ->
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
}
