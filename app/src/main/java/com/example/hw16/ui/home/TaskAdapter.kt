package com.example.hw16.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hw16.databinding.TaskItemBinding
import com.example.hw16.model.TaskItemUiState
import java.util.*

class TaskAdapter(
    val list: ArrayList<TaskItemUiState>,
    private val indexList: ArrayList<Int>? = null,
    private val onClick: (TaskItemUiState) -> Unit = {}
) : RecyclerView.Adapter<TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(inflater, parent, false)
        return TaskHolder(binding, onClick)
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

class TaskHolder(
    private val binding: TaskItemBinding,
    onClick: (TaskItemUiState) -> Unit = {}
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.apply {
            taskItemRoot.setOnClickListener {
                onClick(task!!)
            }
        }
    }

    fun bind(item: TaskItemUiState) {
        binding.task = item
    }
}

class TaskListAdapter(private val indexList: List<Int>? = null, private val onClick: (TaskItemUiState) -> Unit = {}) :
    ListAdapter<TaskItemUiState, TaskHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TaskItemUiState>() {
            override fun areItemsTheSame(
                oldItem: TaskItemUiState,
                newItem: TaskItemUiState
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TaskItemUiState,
                newItem: TaskItemUiState
            ): Boolean {
                if (oldItem.title != newItem.title) return false
                if (oldItem.description != newItem.description) return false
                if (oldItem.deadline != newItem.deadline) return false
                if (oldItem.image_uri != newItem.image_uri) return false
                if (oldItem.state != newItem.state) return false
                return true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskHolder(
            TaskItemBinding.inflate(inflater, parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(getItem(indexList?.get(position) ?: position))
    }
}
