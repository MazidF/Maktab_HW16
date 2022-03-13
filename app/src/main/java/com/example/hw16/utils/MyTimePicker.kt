package com.example.hw16.utils

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import java.lang.Exception
import java.lang.reflect.Field
import java.text.DateFormat
import java.util.*


@SuppressLint("DiscouragedPrivateApi")
class MyTimePicker(
    context: Context,
    callBack: OnTimeSetListener?,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) :
    TimePickerDialog(context, callBack, hourOfDay, minute, is24HourView) {
    private var minHour = -1
    private var minMinute = -1
    private var maxHour = 25
    private var maxMinute = 25
    private var currentHour = 0
    private var currentMinute = 0
    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat: DateFormat
    fun setMin(hour: Int, minute: Int) {
        minHour = hour
        minMinute = minute
    }

    fun setMax(hour: Int, minute: Int) {
        maxHour = hour
        maxMinute = minute
    }

    override fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        var validTime = true
        if (hourOfDay < minHour || hourOfDay == minHour && minute < minMinute) {
            validTime = false
        }
        if (hourOfDay > maxHour || hourOfDay == maxHour && minute > maxMinute) {
            validTime = false
        }
        if (validTime) {
            currentHour = hourOfDay
            currentMinute = minute
        }
        updateTime(currentHour, currentMinute)
        updateDialogTitle(view, currentHour, currentMinute)
    }

    private fun updateDialogTitle(timePicker: TimePicker, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val title: String = dateFormat.format(calendar.getTime())
        setTitle(title)
    }

    init {
        currentHour = hourOfDay
        currentMinute = minute
        dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
        try {
            val superclass = TimePickerDialog::class.java
            val mTimePickerField: Field = superclass.getDeclaredField("mTimePicker")
            mTimePickerField.isAccessible = true
            val mTimePicker = mTimePickerField.get(this) as TimePicker
            mTimePicker.setOnTimeChangedListener(this)
        } catch (e: Exception) { }
    }
}
