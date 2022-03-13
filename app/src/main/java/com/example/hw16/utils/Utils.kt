package com.example.hw16.utils

import android.app.DatePickerDialog
import android.content.Context
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

fun toMillisecond(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
    return Date(year, month, day, hour, minute, 0).time
}

fun Int.format(length: Int = 2): String {
    return String.format("%02d", this)
}