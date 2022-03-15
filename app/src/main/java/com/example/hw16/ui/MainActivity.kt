package com.example.hw16.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hw16.R
import com.example.hw16.databinding.ActivityMainBinding
import com.example.hw16.databinding.TaskMakerBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.Task
import com.example.hw16.utils.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var imageDialog: AlertDialog
    private lateinit var cameraLauncher: ActivityResultLauncher<Void>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private val model: ViewModelMain by viewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        with(binding) {
            bottom.background = null
            initTaskDialog()
            initImageTakerDialog()
            floatingBtn.setOnClickListener {
                dialog.show()
            }
        }
    }

    private fun initImageTakerDialog() {
        imageDialog = AlertDialog.Builder(this)
            .setItems(arrayOf("Camera", "Gallery")) { _, index ->
                if (index == 0) {
                    cameraLauncher.launch(null)
                } else {
                    galleryLauncher.launch("image/*")
                }
            }.create()
    }

    private fun createCameraLauncher(binding: TaskMakerBinding) {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                binding.uri = model.saveToFile(it, filesDir)
            }
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                binding.uri = it.path
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initTaskDialog() {
        val binding = TaskMakerBinding.inflate(layoutInflater).apply {
            uri = ""
            taskMakerCalendar.setOnClickListener {
                model.createPicker(this@MainActivity) { year, month, day, hour, minute ->
                    taskMakerDate.text = "$year/${month.format()}/${day.format()}  &  ${hour.format()}:${minute.format()}"
                    taskMakerDate.tag = toSecond(year, month, day, hour, minute)
                }
            }
            taskMakerCreateBtn.setOnClickListener {
                createTask(this)?.let {
                    model.addTask(it)
                }
            }
            taskMakerImagePicker.setOnClickListener {
                imageDialog.show()
            }
            taskMakerImageRetry.setOnClickListener {
                imageDialog.show()
                File(uri).delete()
            }
            taskMakerImageRemover.setOnClickListener {
                taskMakerImageView.setImageResource(R.drawable.ic_camera)
                uri = ""
            }
        }

        createCameraLauncher(binding)

        dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .setOnDismissListener {
                with(binding) {
                    taskMakerTitle.apply {
                        text?.clear()
                        error = null
                    }
                    taskMakerDescription.apply {
                        text?.clear()
                        error = null
                    }
                    taskMakerDate.apply {
                            text = getText(R.string.time_data)
                            error = null
                        }
                    taskMakerTitle.requestFocus()
                    taskMakerImageView.setImageResource(R.drawable.ic_camera)
                    uri = ""
                }
            }
            .create()
    }

    private fun createTask(binding: TaskMakerBinding): Task? {
        val isValid = check(binding)
        var task: Task? = null
        if (isValid) {
            with(binding) {
                dialog.dismiss()
                task = Task(
                    userName = model.user!!.username,
                    title = taskMakerTitle.text.toString(),
                    description = taskMakerDescription.text.toString(),
                    deadline = taskMakerDate.tag as Long, // TODO: Time should be right :(
                    image_uri = uri ?: ""
                )
            }
        }
        return task
    }

    private fun check(binding: TaskMakerBinding) : Boolean {
        var result = true

        with(binding) {
            taskMakerDate.apply {
                if (text!! == getString(R.string.time_data)) {
                    error = "Choose a date!!"
                    result = false
                    requestFocus()
                }
            }
            taskMakerTitle.apply {
                if (text!!.isBlank()) {
                    error = "Invalid value!!"
                    result = false
                    requestFocus()
                }
            }
        }

        return result
    }
}
