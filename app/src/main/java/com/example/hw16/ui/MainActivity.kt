package com.example.hw16.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.hw16.R
import com.example.hw16.databinding.ActivityMainBinding
import com.example.hw16.databinding.TaskMakerBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.Task
import com.example.hw16.ui.done.FragmentDone
import com.example.hw16.ui.home.FragmentHome
import com.example.hw16.utils.*

class MainActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
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
            viewPager.adapter =
                MyFragmentPagerManager(supportFragmentManager, listOf(FragmentHome::class.java, FragmentDone::class.java))
            tabLayout.setupWithViewPager(viewPager)
            initDialog()
            floatingBtn.setOnClickListener {
                dialog.show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initDialog() {
        val binding = TaskMakerBinding.inflate(layoutInflater).apply {
            taskMakerCalendar.setOnClickListener {
                model.createTask(this@MainActivity) {year, month, day, hour, minute ->
                    taskMakerDate.text = "$year/${month.format()}/${day.format()}  &  ${hour.format()}:${minute.format()}"
                    taskMakerDate.tag = toMillisecond(year, month, day, hour, minute)
                }
            }
            taskMakerCreateBtn.setOnClickListener {
                createTask(this)?.let {
                    model.addTask(it)
                }
            }
        }
        dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .setOnDismissListener {
                with(binding) {
                    taskMakerTitle.text?.clear()
                    taskMakerDescription.text?.clear()
                    taskMakerDate.text = getText(R.string.time_data)
                    taskMakerTitle.requestFocus()
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
                    taskMakerTitle.text.toString(),
                    taskMakerDescription.text.toString(),
                    taskMakerDate.tag as Long,
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
