package com.example.hw16.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hw16.model.TaskItemUiState
import com.example.hw16.ui.home.TaskListAdapter.TaskHolder
import com.example.hw16.utils.IndexedList
import com.example.hw16.utils.logger
import com.example.hw16.utils.setup
import com.example.hw16.databinding.TaskItemBinding as ItemTaskBinding

/*class TaskAdapter(
    val list: ArrayList<TaskItemUiState>,
    private val indexList: ArrayList<Int>? = null,
    private val onClick: (TaskItemUiState) -> Unit = {}
) : RecyclerView.Adapter<TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
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
}*/

class TaskListAdapter(
    /*private var indexList: List<Int>? = null,*/
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

//    override fun getItemCount() = indexList?.size ?: super.getItemCount()

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
/*
    override fun getItem(position: Int): TaskItemUiState {
        return currentList[indexList?.get(position) ?: position]
    }*/

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
//        val safePosition = holder.adapterPosition
        holder.bind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: TaskHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.reset()
    }

    fun customSubmit(list: IndexedList<TaskItemUiState>, commitMessage: String) {
        submitList(list) {
            logger(commitMessage)
        }
        logger("customSubmit")
    }
}

/*
interface DiffFinder<T> {
    fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    fun areContentsTheSame(oldItem: T, newItem: T): Boolean
}

class BindableViewHolder<T, VB: ViewDataBinding>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(t: T)
    companion object {
        fun <T, VB: ViewDataBinding> factory() = Function<Class<VB>, BindableViewHolder<T, VB>> {
            val constructor = it.getConstructor(LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            BindableViewHolder((constructor.newInstance() as VB).root)
        }
    }
}

class GenericListAdapter<T, VH : BindableViewHolder<T>>(
    diffFinder: DiffFinder<T>
) : ListAdapter<T, VH>(genericDiffCallback(diffFinder)) {
    companion object {
        fun <R> genericDiffCallback(diffFinder: DiffFinder<R>): DiffUtil.ItemCallback<R> {
            return object : DiffUtil.ItemCallback<R>() {
                override fun areItemsTheSame(oldItem: R, newItem: R): Boolean {
                    return diffFinder.areItemsTheSame(oldItem, newItem)
                }

                override fun areContentsTheSame(oldItem: R, newItem: R): Boolean {
                    return diffFinder.areContentsTheSame(oldItem, newItem)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position))
    }
}
*/
