package com.example.hw16.data

import androidx.lifecycle.*
import com.example.hw16.data.local.FileLocalDataSource
import com.example.hw16.data.local.TaskDataSource
import com.example.hw16.data.local.UserDataSource
import com.example.hw16.model.Task
import com.example.hw16.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.Serializable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MyRepository(
    private val executors: ExecutorService,
    private val userDataSource: UserDataSource,
    private val taskDataSource: TaskDataSource,
    private val fileDataSource: FileLocalDataSource
) {
    val taskChangeState = MutableLiveData<Pair<LiveData<Task?>, Long>>()

    private fun <T> thread(todo: () -> T?): T? {
        val future: Future<T?> = executors.submit(todo)
        return future.get()
    }

    suspend  fun removeTask(vararg ids: Long) {
        taskDataSource.removeWithId(*ids)
    }

    suspend  fun addTask(task: Task): Flow<Task?> {
        val id = taskDataSource.insert(task)[0]
        return getAndFind(id)
    }

    private suspend  fun getAndFind(id: Long): Flow<Task?> {
        return taskDataSource.find(id).also {
            taskChangeState.postValue(Pair(it.asLiveData(), id))
        }
    }

    suspend  fun searchTasks(
        title: String? = null,
        description: String? = null,
        deadline: Long? = null,
        isDone: Boolean? = null,
        imageUri: String? = null,
    ) : Flow<List<Task>> {
        return taskDataSource.search(title, description, deadline, isDone, imageUri)
    }

    suspend  fun getTask(id: Long): Flow<Task?> {
        return taskDataSource.find(id)
    }

    fun getUserTasks(username: String): Flow<List<Task>> {
        return taskDataSource.getUserTasks(username)
    }

    suspend  fun editTask(task: Task): Flow<Task?> {
        taskDataSource.update(task)
        return getAndFind(task.id!!)
    }

    suspend  fun signInUser(user: User): Flow<User?> {
        userDataSource.insert(user)
        return userDataSource.find(user.username)
    }

    suspend  fun logInUser(userName: String, password: String): LiveData<Pair<Boolean, User?>> {
        return userDataSource.find(userName).asLiveData().map { user ->
            if (user == null) {
                Pair(false, null)
            } else {
                Pair(password == user.password, user)
            }
        }
    }

    //////////////////////// save to file //////////////////////

    fun save(
        file: File, byteArray: ByteArray, override: Boolean = false,
        waitUntilFileIsReady: Boolean = false
    ) {
        fileDataSource.save(file, byteArray, override, waitUntilFileIsReady)
    }

    fun save(
        uri: String, byteArray: ByteArray, override: Boolean = false,
        waitUntilFileIsReady: Boolean = false
    ) {
        save(File(uri), byteArray, override, waitUntilFileIsReady)
    }

    fun load(file: File) {
        fileDataSource.load(file)
    }

    fun load(uri: String) {
        load(File(uri))
    }

    fun <T : Serializable> saveObject(file: File, t: T) {
        fileDataSource.writeObject(file, t)
    }

    fun <T : Serializable> saveObject(file: File): T? {
        return fileDataSource.readObject(file)
    }
}
