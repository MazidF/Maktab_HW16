package com.example.hw16.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw16.R
import com.example.hw16.databinding.FragmentHomeSubBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.TaskItemUiState
import com.example.hw16.model.TaskState.*
import com.example.hw16.ui.App
import com.example.hw16.ui.home.MyItemTouchHelperCallback.Companion.connect
import com.example.hw16.utils.Mapper.toTask
import com.example.hw16.utils.logger
import com.google.android.material.snackbar.Snackbar

class FragmentHomeSub : Fragment() {
    private val navController by lazy {
        findNavController()
    }
    val model: ViewModelHome by activityViewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
    private lateinit var adapter: TaskListAdapter
    lateinit var binding: FragmentHomeSubBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeSubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentHomeSub", requireArguments()["state"].toString() + " view created")
        init()
    }

    private fun init() {
        adapter = TaskListAdapter(onClick = ::onItemClick, onTaskEdit = { it, isDone ->
            logger("$it \n isDone: $isDone")
            model.editTask(it.toTask(model.user.value!!.username, isDone))
        })

        a().observe(viewLifecycleOwner) {
            val name = requireArguments()["state"].toString()
            if (it == null) return@observe
            val list = it
            adapter.customSubmit(list, commitMessage = "callback : $name")
            binding.isEmpty = list.isEmpty()
            logger("$name: $list")
        }

        with(binding) {
            recyclerView.apply {
//                setRecycledViewPool(model.viewPool)
                this.adapter = this@FragmentHomeSub.adapter
                connect(
                    onDrag = this@FragmentHomeSub::onDragItem,
                    onSwipe = this@FragmentHomeSub::onSwipeItem
                )
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun onItemClick(task: TaskItemUiState) {
        navController.navigate(FragmentHomeDirections.actionFragmentHomeToFragmentTask(task.id))
    }

    private fun onDragItem(from: Int, to: Int): Boolean {
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun onSwipeItem(position: Int) {
        val item = adapter.getItem(position)
        model.removeTask(item)
        Snackbar.make(
            requireActivity().findViewById(R.id.coordinator),
            "${item.title} removed!!",
            Snackbar.LENGTH_LONG
        ).apply {
            setAction("Undo") {
                model.addTask(item.toTask(model.user.value!!.username))
            }
        }.show()
    }

    private fun a(): LiveData<ArrayList<TaskItemUiState>> {
        val args = requireArguments()
        with(model) {
            return when (args["state"]) {
                DONE.name -> listDone
                DOING.name -> listDoing
                TODO.name -> listTodo
                else -> tasks
            }
        }
    }

}
