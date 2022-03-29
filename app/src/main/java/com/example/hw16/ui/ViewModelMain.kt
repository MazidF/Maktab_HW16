package com.example.hw16.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw16.data.local.FileType
import com.example.hw16.domain.TaskAndUserUseCase
import com.example.hw16.model.Task
import com.example.hw16.model.User
import com.example.hw16.utils.logger
import com.example.hw16.utils.observeForever
import kotlinx.coroutines.launch

class ViewModelMain(
    private val useCase: TaskAndUserUseCase
) : ViewModel() {

    val error by lazy {
        MutableLiveData<ProgressResult>()
    }
    val user: LiveData<User?> = useCase.user

    fun addTask(task: Task) {
        viewModelScope.launch {
            useCase.addTask(task)
        }
    }

    fun logout() {
        useCase.logout()
    }

    fun login(userName: String, password: String, setError: Boolean = true) {
        logger("login")
        val liveData = useCase.login(userName, password)
        observeForever(liveData) {
            if (it == null) return@observeForever
            if (setError) {
                error.value = it
            }
        }
    }

    fun signIn(user: User, setError: Boolean = true) {
        logger("sign_in")
        viewModelScope.launch {
            observeForever(useCase.signIn(user)) {
                if (it == null) return@observeForever
                if (setError) {
                    error.value = it
                }
            }
        }
    }

    fun saveImageToFile(context: Context, fileType: FileType, bitmap: Bitmap): String? {
        return useCase.saveImageToFile(context, fileType, bitmap)
    }

    fun removeFile(uri: String) {
        useCase.removeFile(uri)
    }
}