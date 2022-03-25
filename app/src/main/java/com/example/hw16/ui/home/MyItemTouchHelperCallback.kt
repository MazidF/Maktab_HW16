package com.example.hw16.ui.home

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

class MyItemTouchHelperCallback(
    val onDrag: (Int, Int) -> Boolean = { _, _ -> false },
    val onSwipe: (Int) -> Unit = {}
) : ItemTouchHelper.SimpleCallback(UP or DOWN or RIGHT or LEFT, LEFT) {
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
        onSwipe(position)
    }

    companion object {
        fun RecyclerView.connect(
            onDrag: (Int, Int) -> Boolean = { _, _ -> false },
            onSwipe: (Int) -> Unit = {}
        ) {
            ItemTouchHelper(MyItemTouchHelperCallback(onDrag, onSwipe))
                .attachToRecyclerView(this)
        }
    }
}