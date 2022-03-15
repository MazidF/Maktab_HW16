package com.example.hw16.ui.home

import com.example.hw16.model.TaskState

sealed class NotifyDataChange {
    class Insert(val position: Int, val size: Int = 1) : NotifyDataChange()
    class Delete(val position: Int, val size: Int = 1) : NotifyDataChange()
    class Notify(val state: TaskState? = null) : NotifyDataChange()
}