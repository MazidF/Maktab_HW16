package com.example.hw16.data

import androidx.lifecycle.MutableLiveData
import com.example.hw16.data.local.FileLocalDataSource
import com.example.hw16.data.local.TaskDataSource
import com.example.hw16.data.local.UserDataSource
import com.example.hw16.model.Task
import com.example.hw16.model.User
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
    val taskChangeState = MutableLiveData<Pair<Task?, Long>>()

    private fun <T> thread(todo: () -> T?): T? {
        val future: Future<T?> = executors.submit(todo)
        return future.get()
    }

    fun removeTask(vararg ids: Long) {
        thread {
            taskDataSource.removeWithId(*ids)
        }
    }

    fun addTask(task: Task): Task {
        return thread {
            val id = taskDataSource.insert(task)[0]
            getAndFind(id)
        }!!
    }

    private fun getAndFind(id: Long): Task? {
        return taskDataSource.find(id).also {
            taskChangeState.postValue(Pair(it, id))
        }
    }

    fun searchTasks(
        title: String? = null,
        description: String? = null,
        deadline: Long? = null,
        isDone: Boolean? = null,
        imageUri: String? = null,
    ) : List<Task> {
        return thread {
            taskDataSource.search(title, description, deadline, isDone, imageUri)
        }!!
    }

    fun getTask(id: Long): Task? {
        return thread {
            taskDataSource.find(id)
        }
    }

    fun getUserTasks(username: String): List<Task> {
        return thread {
            val result = taskDataSource.getUserTasks(username)
            result
        }!!
    }

    fun editTask(task: Task): Task {
        return thread {
            taskDataSource.update(task)
            getAndFind(task.id!!)
        }!!
    }

    fun signInUser(user: User): User {
        return thread {
            userDataSource.insert(user)
            userDataSource.find(user.username)
        }!!
    }

    fun logInUser(userName: String, password: String): Pair<Boolean, User?> {
        val user = thread { userDataSource.find(userName) }
        return if (user != null && user.password == password) {
            Pair(true, user)
        } else {
            Pair(false, null)
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
