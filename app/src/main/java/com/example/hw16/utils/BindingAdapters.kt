package com.example.hw16.utils

import android.content.res.ColorStateList
import android.graphics.Color.*
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.hw16.R
import com.example.hw16.model.TaskState
import com.ms.square.android.expandabletextview.ExpandableTextView
import java.util.*


@BindingAdapter("app:image", "app:imageDrawable", requireAll = false)
fun setImageFromUri(imageView: ImageView, uri: String?, drawable: Drawable? = null) {
    if (uri != null) {
        Glide.with(imageView.context.applicationContext) // TODO: search about applicationContext
            .load(uri)
            .placeholder(R.drawable.loading_animation)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.visibility = GONE
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ) = false
            })
            .into(imageView)
    } else {
        imageView.setImageDrawable(drawable)
    }
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, bool: Boolean) {
    view.isVisible = bool
}

@BindingAdapter("app:setText")
fun setText(view: ExpandableTextView, text: String) {
    view.text = text
}

@BindingAdapter("app:stateColor")
fun stateColor(view: View, state: TaskState) {
    val color = when(state) {
        TaskState.DONE -> GREEN
        TaskState.DOING -> BLUE
        TaskState.TODO -> RED
    }
    view.setBackgroundColor(color)
}

@BindingAdapter("app:drawLine")
fun drawLine(textView: TextView, bool: Boolean) {
    textView.paintFlags = if (bool) {
        textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags
    }
}

@BindingAdapter("app:radioColorEnable", "app:radioColorDisable")
fun setColorForRadioButton(radioButton: RadioButton, disable: String, enable: String) {
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled)
        ), intArrayOf(
            parseColor(disable),  //disabled
            parseColor(enable) //enabled
        )
    )
    radioButton.buttonTintList = colorStateList
}

@BindingAdapter("app:date")
fun setDateInView(textView: TextView, date: Date?) {
    date?.run {
        val isToday = isToday(year, month, day)
        textView.text = getTimeAsString(date, isToday)
    }
}

@BindingAdapter("app:setup")
fun RadioButton.setup(default: Boolean = isChecked) {
    isSelected = default
    isChecked = default
    setOnClickListener {
        if (isSelected) {
            isChecked = false
            isSelected = false
        } else {
            isSelected = true
            isChecked = true
        }
    }
}