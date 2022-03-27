package com.example.hw16.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.example.hw16.model.Task
import com.example.hw16.model.TaskState
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
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

fun getTime(deadline: Long): String {
    val date = Date(deadline)
    return with(date) {
        "${year + 1900}/${month.format()}/${day.format()}  &  ${hours.format()}:${minutes.format()}"
    }
}

@SuppressLint("SimpleDateFormat")
fun getDeadline(time: String): Long {
    return SimpleDateFormat("yyyy/MM/dd  &  hh:mm").parse(time)?.time ?: -1
}

fun <T> observeForever(liveData: LiveData<T>, obs: Observer<T>) {
    var observer: Observer<T>? = null
    observer = Observer {
        obs.onChanged(it)
        liveData.removeObserver(observer!!)
    }
    liveData.observeForever(observer)
}

fun ActivityResultCaller.createImageLauncher(
    camera: (Bitmap?) -> Unit,
    gallery: (Uri?) -> Unit
): Pair<ActivityResultLauncher<Void>, ActivityResultLauncher<String>> {
    val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        camera(it)
    }
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        gallery(it)
    }
    return Pair(cameraLauncher, galleryLauncher)
}

fun NavController.popUpToNavigate(
    dest: Int,
    inclusive: Boolean,
    destination: NavDirections
) {
    val navOption = NavOptions.Builder()
        .setPopUpTo(dest, inclusive)
        .build()
    navigate(
        destination,
        navOptions = navOption
    )
}

fun logger(msg: String) {
    Log.d("app_log", msg)
}

fun showSnackBar(activity: Activity, message: String?, duration: Int = Snackbar.LENGTH_LONG) {
    val rootView: View = activity.window.decorView.findViewById(android.R.id.content)
    Snackbar.make(rootView, message!!, duration).show()
}