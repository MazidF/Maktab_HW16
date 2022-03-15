package com.example.hw16.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw16.databinding.TaskItemBinding
import com.example.hw16.model.Task
import com.example.hw16.model.TaskItemUiState
import java.util.*
import kotlin.collections.ArrayList

class TaskAdapter(
    val list: ArrayList<TaskItemUiState>,
    private val indexList: ArrayList<Int>? = null,
    private val onClick: (TaskItemBinding, TaskItemUiState) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    inner class TaskHolder(private val binding: TaskItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskItemUiState) {
            binding.task = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskHolder(TaskItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(list[indexList?.get(position) ?: position])
    }

    override fun getItemCount() = indexList?.size ?: list.size

    fun remove(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun remove(item: TaskItemUiState) {
        remove(list.indexOf(item))
    }

    fun add(item: TaskItemUiState) {
        list.add(item)
        notifyItemInserted(list.size - 1)
    }

    fun add(item: TaskItemUiState, position: Int) {
        list.add(position, item)
        notifyItemInserted(position)
    }

    fun swap(from: Int, to: Int) {
        Collections.swap(list, from, to)
        notifyItemMoved(from, to)
    }
}
