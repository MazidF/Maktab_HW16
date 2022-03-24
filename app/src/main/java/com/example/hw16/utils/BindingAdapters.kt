package com.example.hw16.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.hw16.R
import com.ms.square.android.expandabletextview.ExpandableTextView

@BindingAdapter("app:image")
fun setImageFromUri(imageView: ImageView, uri: String?) {
    if (uri != null && uri != "") {
        Glide.with(imageView.context)
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
        imageView.setImageDrawable(null)
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
