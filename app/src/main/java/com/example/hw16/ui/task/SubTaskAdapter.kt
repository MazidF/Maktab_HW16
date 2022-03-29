package com.example.hw16.ui.task

import android.annotation.SuppressLint
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
    private var list: List<SubTaskItemUiState>,
    private val onRemoveItem: (SubTaskItemUiState) -> Unit = {},
    private val onClick: (SubTaskItemUiState) -> Unit = {}
) : RecyclerView.Adapter<SubTaskAdapter.SubTaskHolder>() {

    private val diffCallback = SubTaskDiffCallback(list, listOf())

    fun getList() = list

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
        return SubTaskHolder(binding.apply { subTaskTitle.requestFocus() })
    }

    override fun onBindViewHolder(holder: SubTaskHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(updateList: List<SubTaskItemUiState>) {
        list = updateList
//        val diff = diffCallback.submitList(updateList)
//        diff.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    fun moveItem(from: Int, to: Int) {
        Collections.swap(list, from, to)
        notifyItemMoved(from, to)
    }

    fun remove(position: Int) {
        if (position >= 0) {
            val item = list[position]
            onRemoveItem(item)
        }
    }
}
