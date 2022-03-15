package com.example.hw16.utils

import android.app.DatePickerDialog
import android.content.Context
import com.example.hw16.model.Task
import com.example.hw16.model.TaskState
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import java.util.*
import java.util.Calendar.*


fun createTimePicker(context: Context, isTodaySelected: Boolean, callback: (Int, Int) -> Unit) {
    val calendar = getCalendar()
    MyTimePicker(context, { _, hour, minute ->
        callback(hour, minute)
    }, calendar[HOUR], calendar[MINUTE], true).apply {
        if (isTodaySelected) {
            setMin(calendar[HOUR], calendar[MINUTE])
        }
    }.show()
}

fun createDatePicker(context: Context, callback: (Int, Int, Int) -> Unit) {
    val calendar = getCalendar()
    DatePickerDialog(context, { _, year, month, day ->
        callback(year, month + 1, day)
    }, calendar[YEAR], calendar[MONTH], calendar[DAY_OF_MONTH]).apply {
        datePicker.minDate = calendar.timeInMillis
    }.show()
}

fun getCalendar(): Calendar = getInstance()

fun isToday(year: Int, month: Int, day: Int, calendar: Calendar = getCalendar()): Boolean {
    return year == calendar[YEAR] && month == calendar[MONTH] && day == calendar[DAY_OF_MONTH]
}

fun toSecond(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
    val cal = getInstance()
    cal[MINUTE] = minute
    cal[HOUR_OF_DAY] = hour
    cal[DAY_OF_MONTH] = day
    cal[MONTH] = month
    cal[YEAR] = year
    return cal.timeInMillis / 1000
}

fun Int.format(length: Int = 2): String {
    return String.format("%02d", this)
}

fun Task.getState(isDone: Boolean, deadline: Long): TaskState {
    return if (isDone) {
        TaskState.DONE
    } else {
        if (deadline > System.currentTimeMillis()) {
            TaskState.DOING
        } else {
            TaskState.TODO
        }
    }
}

fun Task.getTime(deadline: Long): String {
    val date = Date(deadline)
    return with(date) {
        "${year + 1900}/${month.format()}/${day.format()}  &  ${hours.format()}:${minutes.format()}"
    }
}