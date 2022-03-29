package com.example.hw16.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hw16.model.TaskItemUiState
import com.example.hw16.ui.home.TaskListAdapter.TaskHolder
import com.example.hw16.utils.logger
import com.example.hw16.utils.setup
import com.example.hw16.databinding.TaskItemBinding as ItemTaskBinding

class TaskListAdapter(
    private val onTaskEdit: (TaskItemUiState, Boolean) -> Unit,
    private val onClick: (TaskItemUiState) -> Unit = {}
) :
    ListAdapter<TaskItemUiState, TaskHolder>(DIFF_CALLBACK) {

    inner class TaskHolder(
        private val binding: ItemTaskBinding,
        onClick: (TaskItemUiState) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                taskItemRoot.setOnClickListener {
                    onClick(task()!!)
                }
            }
        }

        fun task(): TaskItemUiState? = binding.task

        fun bind(item: TaskItemUiState) {
            logger(item.title + " bonded!! at : " + this.adapterPosition)
            binding.taskItemRadioButton.setup { isChecked ->
                item.let {
                    if (it.isDone() != isChecked) {
                        onTaskEdit(it, isChecked)
                    }
                }
            }
            binding.task = item
            binding.taskItemRadioButton.isChecked = item.isDone()
            binding.taskItemRadioButton.isSelected = item.isDone()
        }

        fun reset() {
            with(binding) {
                task = null
                taskItemRadioButton.setOnCheckedChangeListener(null)
            }
        }
    }

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
            ItemTaskBinding.inflate(inflater, parent, false)/*.apply {
                 taskItemRadioButton.setup { isChecked ->
                     task?.let {
                         if (it.isDone() != isChecked) {
                             onTaskEdit(it, isChecked)
                         }
                     }
                 }
            }*/,
            onClick
        )
    }

    public override fun getItem(position: Int): TaskItemUiState {
        return super.getItem(position)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: TaskHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.reset()
    }

    fun customSubmit(list: List<TaskItemUiState>, commitMessage: String) {
        submitList(list) {
            logger(commitMessage)
        }
        logger("customSubmit")
    }
}
