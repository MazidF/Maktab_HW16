package com.example.hw16.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.hw16.data.MyRepository
import com.example.hw16.model.Task
import com.example.hw16.model.User
import com.example.hw16.utils.createDatePicker
import com.example.hw16.utils.createTimePicker
import com.example.hw16.utils.isToday
import java.io.ByteArrayOutputStream
import java.io.File

class ViewModelMain(
    private val repository: MyRepository
) : ViewModel() {

    companion object {
        const val IMAGE_FILE = "images"
        const val VOICE_FILE = "voice"
    }

    var user: User? = User(0, "mock user", "mock password")
    private set

    fun createPicker(context: Context, cb: (Int, Int, Int, Int, Int) -> Unit) {
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
        repository.signInUser(user)
        this.user = user
    }

    fun saveToFile(bitmap: Bitmap, filesDir: File): String? {
        val output = ByteArrayOutputStream()
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)) {
            return null
        }
        val root = File(filesDir, IMAGE_FILE)
        if (root.exists().not()) {
            root.mkdirs()
        }
        val file = File(root, System.currentTimeMillis().toString() + ".png")
        repository.save(file, output.toByteArray(), waitUntilFileIsReady = true)
        return file.absolutePath
    }
}