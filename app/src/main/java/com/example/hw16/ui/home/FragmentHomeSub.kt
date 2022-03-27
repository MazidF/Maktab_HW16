package com.example.hw16.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw16.R
import com.example.hw16.databinding.FragmentHomeSubBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.TaskState.*
import com.example.hw16.ui.App
import com.example.hw16.ui.home.MyItemTouchHelperCallback.Companion.connect
import com.example.hw16.utils.Mapper.toTask
import com.example.hw16.utils.logger
import com.google.android.material.snackbar.Snackbar

class FragmentHomeSub : Fragment() {
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
        adapter = TaskListAdapter()

        model.tasks.observe(viewLifecycleOwner) {
            logger("model.task.observe")
            if (it == null) return@observe
            val indexList = getIndexList()
            adapter.customSubmit(it, indexList)
            binding.isEmpty = indexList?.isEmpty() ?: it.isEmpty()
        }

        with(binding) {
            recyclerView.apply {
                setRecycledViewPool(model.viewPool)
                this.adapter = this@FragmentHomeSub.adapter
                connect(
                    onDrag = this@FragmentHomeSub::onDragItem,
                    onSwipe = this@FragmentHomeSub::onSwipeItem
                )
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun onDragItem(from: Int, to: Int): Boolean {
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun onSwipeItem(position: Int) {
        val item = adapter.currentList[position]
        model.removeTask(item)
        Snackbar.make(
            requireActivity().findViewById(R.id.coordinator),
            "${item.title} removed!!",
            Snackbar.LENGTH_LONG
        ).apply {
            setAction("Undo") {
                model.addTask(item.toTask(model.user.value!!.username))
            }
/*            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    model.removeImage(item.image_uri)
                }
            })*/
        }.show()
    }

    private fun getIndexList(): List<Int>? {
        val args = requireArguments()
        return when (args["state"]) {
            DONE.name -> model.listDone
            DOING.name -> model.listDoing
            TODO.name -> model.listTodo
            else -> null
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        Log.d("FragmentHomeSub", requireArguments()["state"].toString() + " resumed")
    }

    override fun onStop() {
        super.onStop()
        Log.d("FragmentHomeSub", requireArguments()["state"].toString() + " stoped")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FragmentHomeSub", requireArguments()["state"].toString() + " created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("FragmentHomeSub", requireArguments()["state"].toString() + " destroyed")
    }

}
