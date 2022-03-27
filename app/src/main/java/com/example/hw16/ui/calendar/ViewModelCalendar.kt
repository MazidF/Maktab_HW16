package com.example.hw16.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hw16.domain.TaskAndUserUseCase
import com.example.hw16.model.Task

class ViewModelCalendar(
    private val useCase: TaskAndUserUseCase
) : ViewModel() {
    fun getTaskOfDay(from: Long, to: Long) : LiveData<List<Task>> {
        return useCase.getTaskOfDay(from, to)
    }
}
