package com.example.hw16.ui.task

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hw16.databinding.FragmentTaskBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.SubTask
import com.example.hw16.model.Task
import com.example.hw16.model.TaskWithSubTask
import com.example.hw16.ui.App
import com.example.hw16.utils.Mapper.toSubTask
import com.example.hw16.utils.Mapper.toSubTaskItemUiState
import com.example.hw16.utils.createPicker
import com.example.hw16.utils.observeForever
import com.example.hw16.utils.toDate
import java.util.*

class FragmentTask : Fragment() {
    private lateinit var adapter: SubTaskAdapter
    private val model: ViewModelTask by viewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
    private val args: FragmentTaskArgs by navArgs()
    lateinit var binding: FragmentTaskBinding
    private val deadline by lazy {
        MutableLiveData<Date>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        loadTask()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(taskWithSubTask: TaskWithSubTask) {
        deadline.value = taskWithSubTask.task.deadline
        with(binding) {
            date = deadline
            lifecycleOwner = viewLifecycleOwner
            if (taskWithSubTask.task.isDone) {
                root.setOnTouchListener { _, _ -> true }
            }
            recyclerInit(taskWithSubTask.task.id)
            taskDeadline.setOnClickListener {
                createPicker(requireContext()) { year, month, day, hour, minute ->
                    deadline.value = toDate(year, month, day, hour, minute)
                }
            }
        }
    }

    private fun recyclerInit(ownerId: Long) {
        adapter = SubTaskAdapter(arrayListOf(), onRemoveItem = {
            model.removeSubTask(ownerId, it)
            binding.taskAddSubTask.requestFocus()
        })
        model.subTasks.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it.map { subTask ->
                    subTask.toSubTaskItemUiState()
                })
                binding.taskAddSubTask.requestFocus()
            }
        }
        with(binding.taskSubTaskList) {
            this.adapter = this@FragmentTask.adapter
            layoutManager = GridLayoutManager(requireContext(), 1)
            SubTaskItemHelper(::onDrag, ::onSwipe).connect(this)
        }
        binding.taskAddSubTask.setOnClickListener {
            addSubTask(ownerId, "", adapter.itemCount)
        }
        model.getSubTasks(ownerId)
    }

    private fun onSwipe(position: Int, direction: Int) {

    }

    private fun onDrag(from: Int, to: Int): Boolean {
        adapter.moveItem(from, to)
        return true
    }

    private fun addSubTask(ownerId: Long, subTaskTitle: String, position: Int): SubTask {
        val result = SubTask(
            ownerId = ownerId,
            title = subTaskTitle,
            isDone = false,
            position = position
        )
        model.addSubTask(result)
        return result
    }

    private fun loadTask() {
        val id = args.taskId
        observeForever(model.getTaskWithSubTasks(id)) {
            if (it == null) {
                showError()
            } else {
                binding.taskWithSubTasks = it
                init(it)
            }
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "Task doesn't exist!!", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        saveChanges()
    }

    private fun saveChanges() {
        binding.taskWithSubTasks?.run {
            val newTask = createTask()
            newTask.let {
                model.editTask(it)
            }
            updateSubTasks(task.id)
        }
    }

    private fun updateSubTasks(ownerId: Long) {
        val list = adapter.getList().filter {
            it.title.isNotBlank()
        }.mapIndexed { i, it ->
            it.toSubTask(ownerId, i)
        }
        model.editSubTasks(*list.toTypedArray())
    }

    private fun createTask(): Task {
        with(binding) {
            var (userName, title, description, deadline, image_uri, isDone, id) = binding.taskWithSubTasks!!.task
            taskTitle.text!!.run {
                if (isNotBlank()) {
                    title = toString()
                }
            }
            description = taskDescription.text!!.toString()
            deadline = this@FragmentTask.deadline.value!!
            // TODO: Edit IMAGE_URI
            // TODO: Edit IS_DONE
            return Task(userName, title, description, deadline, image_uri, isDone, id)
        }
    }
}