package com.example.hw16.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.hw16.data.MyRepository
import com.example.hw16.data.local.FileType
import com.example.hw16.model.Task
import com.example.hw16.model.User
import com.example.hw16.utils.createDatePicker
import com.example.hw16.utils.createTimePicker
import com.example.hw16.utils.isToday
import com.example.hw16.utils.observeForever
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import com.example.hw16.ui.ProgressResult.*

class ViewModelMain(
    private val repository: MyRepository
) : ViewModel() {

    val error by lazy {
        MutableLiveData<ProgressResult>()
    }
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun createPicker(context: Context, cb: (Int, Int, Int, Int, Int) -> Unit) {
        createDatePicker(context) { year, month, day ->
            createTimePicker(context, isToday(year, month, day)) { hour, minute ->
                cb(year, month, day, hour, minute)
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun login(userName: String, password: String) {
        val liveData = repository.logInUser(userName, password)
        observeForever(liveData) {
            if (it.first) {
                it.second?.let { user ->
                    _user.value = user
                    error.value = SUCCESS
                    return@observeForever
                }
            }
            error.value = FAIL
        }
    }

    fun logout() {
        _user.value = null
    }

    fun signIn(user: User) {
        viewModelScope.launch {
            observeForever(repository.signInUser(user)) {
                if (it != null) {
                    _user.value = it
                    error.value = SUCCESS
                } else {
                    error.value = FAIL
                }
            }
        }
    }

    fun saveImageToFile(context: Context, fileType: FileType, bitmap: Bitmap): String? {
        val output = ByteArrayOutputStream()
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)) {
            return null
        }
        val fileName = System.currentTimeMillis().toString()
        return repository.save(context, fileType, fileName, output.toByteArray(), waitUntilFileIsReady = true)
    }

    fun removeFile(uri: String) {
        repository.removeFile(uri)
    }
}