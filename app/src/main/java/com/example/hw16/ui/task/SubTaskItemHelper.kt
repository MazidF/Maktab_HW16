package com.example.hw16.ui.task

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView

class SubTaskItemHelper(
    private val onDrag: (Int, Int) -> Boolean,
    private val onSwipe: (Int, Int) -> Unit
): ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        return onDrag(from, to)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onSwipe(position, direction)
    }

    fun connect(recyclerView: RecyclerView) {
        ItemTouchHelper(this).attachToRecyclerView(recyclerView)
    }
}
