package com.example.hw16.ui.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hw16.databinding.SubTaskItemBinding
import com.example.hw16.model.SubTaskItemUiState
import com.example.hw16.utils.setup
import java.util.*


class SubTaskAdapter(
    private var list: ArrayList<SubTaskItemUiState>,
    private val viewHolderApply: (SubTaskHolder) -> Unit = {},
    private val onClick: (SubTaskItemUiState) -> Unit = {}
) : RecyclerView.Adapter<SubTaskAdapter.SubTaskHolder>() {

    private var lastSubTask: SubTaskItemUiState? = null
    private val diffCallback = SubTaskDiffCallback(list, listOf())

    class SubTaskDiffCallback(
        var newList: List<SubTaskItemUiState>,
        var oldList: List<SubTaskItemUiState>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newList[newItemPosition]
            val oldItem = oldList[oldItemPosition]
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newList[newItemPosition]
            val oldItem = oldList[oldItemPosition]
            if (newItem.title != oldItem.title) return false
            if (newItem.isDone != oldItem.isDone) return false
            if (newItem.position != oldItem.position) return false
            return true
        }

        fun submitList(updateList: List<SubTaskItemUiState>): DiffUtil.DiffResult {
            val temp = newList
            newList = updateList
            oldList = temp
            return DiffUtil.calculateDiff(this)
        }
    }

    inner class SubTaskHolder(val binding: SubTaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding) {
                focus = false
                root.setOnClickListener {
                    subTask?.let(onClick)
                }
                subTaskRadioBtn.setup {
                    subTask?.run {
                        isDone = it
                    }
                }
                subTaskTitle.apply {
                    setOnFocusChangeListener { _, hasFocus ->
                        focus = hasFocus
                    }
                    doOnTextChanged { text, _, _, _ ->
                        subTask?.title = text.toString()
                    }
                }
                subTaskEndIcon.setOnClickListener {
                    if (focus == true) { // delete SubTask
                        remove(this@SubTaskHolder.adapterPosition)
                    } else { // drag
                        root.performLongClick()
                    }
                }
            }
        }

        fun bind(item: SubTaskItemUiState) {
            with(binding) {
                subTask = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskHolder {
        val binding = SubTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubTaskHolder(binding.apply { subTaskTitle.requestFocus() }).apply(viewHolderApply)
    }

    override fun onBindViewHolder(holder: SubTaskHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun submitList(updateList: List<SubTaskItemUiState>) {
        submitList(ArrayList(updateList))
    }

    fun submitList(updateList: ArrayList<SubTaskItemUiState>) {
        list = updateList
        val diff = diffCallback.submitList(updateList)
        diff.dispatchUpdatesTo(this)
    }

    fun moveItem(from: Int, to: Int) {
        Collections.swap(list, from, to)
        notifyItemMoved(from, to)
    }

    fun addItem(subTaskItemUiState: SubTaskItemUiState) {
        lastSubTask?.run {
            if (title.isBlank()) {
                removeLastItem()
            }
        }
        lastSubTask = subTaskItemUiState
        list.add(subTaskItemUiState)
        notifyItemInserted(list.lastIndex)
    }

    fun remove(position: Int) {
        if (position >= 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun removeLastItem() {
        remove(list.size - 1)
        lastSubTask = if (list.isNotEmpty()) {
            list.last()
        } else {
            null
        }
    }
}
