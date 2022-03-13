package com.example.hw16.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView

@BindingAdapter("app:image")
fun setImageFromUri(imageView: ImageView, uri: String?) {
    if (uri != null && uri.isNotBlank()) {
        Glide.with(imageView.context)
            .load(uri)
            .into(imageView)
    }
}

@BindingAdapter("app:isVisible")
fun setImageFromUri(view: View, bool: Boolean) {
    view.isVisible = bool
}

@BindingAdapter("app:setText")
fun setImageFromUri(view: ExpandableTextView, text: String) {
    view.text = text
}
