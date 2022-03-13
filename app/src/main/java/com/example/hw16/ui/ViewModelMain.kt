package com.example.hw16.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hw16.data.MyRepository
import com.example.hw16.model.Task
import com.example.hw16.model.User
import com.example.hw16.utils.createDatePicker
import com.example.hw16.utils.createTimePicker
import com.example.hw16.utils.isToday

class ViewModelMain(
    private val repository: MyRepository
) : ViewModel() {

    private var user: User? = null

    fun createTask(context: Context, cb: (Int, Int, Int, Int, Int) -> Unit) {
        createDatePicker(context) { year, month, day ->
            createTimePicker(context, isToday(year, month, day)) { hour, minute ->
                cb(year, month, day, hour, minute)
            }
        }
    }

    fun addTask(task: Task) {
        repository.addTask(task)
    }

    fun login(userName: String, password: String): Boolean {
        val(has, user) = repository.logInUser(userName, password)
        this.user = user
        return has
    }

    fun signIn(user: User) {
//        this.user = repository.signInUser(user)
    }
}