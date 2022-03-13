package com.example.hw16.data

import com.example.hw16.data.local.db.MyDao
import com.example.hw16.model.Task
import com.example.hw16.model.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MyRepository(
    private val executors: ExecutorService,
    private val userDao: MyDao<User, String>,
    private val taskDao: MyDao<Task, Long>
) {

    private fun <T> thread(todo: () -> T?) : T? {
        val future: Future<T?> = executors.submit(todo)
        return future.get()
    }

    fun addTask(task: Task) {
        thread { taskDao.insertItem(task) }
    }

    fun signInUser(user: User) {
        thread { userDao.insertItem(user) }
    }

    fun logInUser(userName: String, password: String): Pair<Boolean, User?> {
        val user = thread { userDao.find(userName) }
        return if (user != null && user.password == password) {
            Pair(true, user)
        } else {
            Pair(false, null)
        }
    }
}
