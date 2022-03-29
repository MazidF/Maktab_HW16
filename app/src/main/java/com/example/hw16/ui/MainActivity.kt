package com.example.hw16.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.hw16.R
import com.example.hw16.data.local.FileType
import com.example.hw16.databinding.ActivityMainBinding
import com.example.hw16.databinding.HeaderLayoutBinding
import com.example.hw16.databinding.TaskMakerBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.Task
import com.example.hw16.ui.home.FragmentHomeDirections
import com.example.hw16.utils.createPicker
import com.example.hw16.utils.format
import com.example.hw16.utils.logger
import com.example.hw16.utils.toDate
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
    }

    private val navController by lazy {
        findNavController(R.id.container)
    }
    private lateinit var dialog: AlertDialog
    private lateinit var imageDialog: AlertDialog
    private lateinit var cameraLauncher: ActivityResultLauncher<Void>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private val model: ViewModelMain by viewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MyFragmentFactory()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        actionBarInit()
        with(binding) {
            NavigationUI.setupWithNavController(bottom, navController)
            user = model.user
            lifecycleOwner = this@MainActivity
            navigationView.addHeaderView(headerInit())
            val userPair = load()
            if (userPair != null) {
                val (username, password) = userPair
                model.login(username, password, false)
            } else {
                navigateToLogin()
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            model.user.observe(this@MainActivity) {
                if (it == null) {
                    navigateToLogin()
                }
                supportActionBar?.setDisplayHomeAsUpEnabled(it != null)
            }
            navigationView.menu.apply {
                getItem(0).setOnMenuItemClickListener {
                    drawer.close()
                    model.logout()
                    false
                }
            }
            bottom.background = null
            initTaskDialog()
            initImageTakerDialog()
            floatingBtn.setOnClickListener {
                dialog.show()
            }
        }
    }

    private fun navigateToLogin() {
        val navOption = NavOptions.Builder()
            .setPopUpTo(R.id.fragmentHome, true)
            .build()
        navController.navigate(
            FragmentHomeDirections.actionFragmentHomeToFragmentLogin(), navOptions = navOption
        )
    }

    private fun actionBarInit() {
        supportActionBar?.apply {
            title = "Task Manager"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        with(binding.drawer) {
            if (isOpen) {
                close()
            } else {
                open()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (binding.drawer.isOpen) {
            binding.drawer.close()
            return
        }
        super.onBackPressed()
    }

    private fun headerInit(): View {
        val binding = HeaderLayoutBinding.inflate(layoutInflater)
        binding.apply {
            user = model.user
            lifecycleOwner = this@MainActivity
        }
        return binding.root
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

    private fun createLauncher(binding: TaskMakerBinding) {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                binding.uri = model.saveImageToFile(this, FileType.IMAGE_FILE, it) ?: ""
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
                createPicker(this@MainActivity) { year, month, day, hour, minute ->
                    taskMakerDate.text =
                        "$year/${month.format()}/${day.format()}  &  ${hour.format()}:${minute.format()}"
                    taskMakerDate.tag = toDate(year, month, day, hour, minute)
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
                model.removeFile(uri!!)
            }
            taskMakerImageRemover.setOnClickListener {
                taskMakerImageView.setImageResource(R.drawable.ic_camera)
                model.removeFile(uri!!)
                uri = ""
            }
        }

        createLauncher(binding)

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
                    userName = model.user.value!!.username,
                    title = taskMakerTitle.text.toString(),
                    description = taskMakerDescription.text.toString(),
                    deadline = taskMakerDate.tag as Date,
                    image_uri = uri ?: ""
                )
            }
        }
        return task
    }

    private fun check(binding: TaskMakerBinding): Boolean {
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

    private fun save() {
        logger("save")
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.apply {
            clear()
            model.user.value?.let {
                logger("user exist!!")
                putString(USERNAME, it.username)
                putString(PASSWORD, it.password)
            }
        }
        edit.apply()
    }

    private fun load() : Pair<String, String>? {
        logger("load")
        var result: Pair<String, String>? = null
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        sharedPreferences.run {
            val username = getString(USERNAME, null) ?: return@run
            val password = getString(PASSWORD, null) ?: throw Exception("pass is lost!!")
            result = Pair(username, password)
        }
        return result
    }

    override fun onStop() {
        super.onStop()
        save()
    }
}
