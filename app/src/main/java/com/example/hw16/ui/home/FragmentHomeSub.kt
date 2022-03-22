package com.example.hw16.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.hw16.databinding.FragmentHomeSubBinding
import com.example.hw16.databinding.SnackbarOneBtnBinding
import com.example.hw16.di.MyViewModelFactory
import com.example.hw16.model.TaskItemUiState
import com.example.hw16.model.TaskState.*
import com.example.hw16.ui.App
import com.example.hw16.ui.home.MyItemTouchHelperCallback.Companion.connect
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class FragmentHomeSub : Fragment() {
    private lateinit var adapter: TaskListAdapter
    val model: ViewModelHome by activityViewModels(factoryProducer = {
        MyViewModelFactory(App.serviceLocator)
    })
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

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        initDialog()

        adapter = TaskListAdapter()

        model.notifyState.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            adapter.submitList(getList())
            model.notifyState.value = null
        }

        with(binding) {
            recyclerView.apply {
                setRecycledViewPool(model.viewPool)
                this.adapter = this@FragmentHomeSub.adapter
                connect(
                    onDrag = this@FragmentHomeSub::onDragItem,
                    onSwipe = this@FragmentHomeSub::onSwipeItem
                )
                layoutManager = StaggeredGridLayoutManager(2, VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                }
            }
        }
    }

    private fun onDragItem(from: Int, to: Int): Boolean {
        adapter.swap(from, to)
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun onSwipeItem(position: Int) {
        val item = adapter.currentList[position]
        adapter.remove(position)

        val view = SnackbarOneBtnBinding.inflate(layoutInflater).apply {
            snackbarBtn.setOnClickListener {
                adapter.add(item, position)
            }
            snackbarMessage.apply {
                text = "${item.title} removed!!"
            }
        }.root
        Snackbar.make(
            view, "Notice!!", Snackbar.LENGTH_LONG
        ).apply {
            setAction("dismiss") {
                model.removeItems(item)
            }
        }.show()
    }

    private fun initDialog() {

    }

    private fun getList(): ArrayList<TaskItemUiState> {
        val args = requireArguments()
        return when (args["state"]) {
            DONE.name -> model.listDone
            DOING.name -> model.listDoing
            TODO.name -> model.listTodo
            else -> model.taskList.value!!
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
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
