package com.hr.test.view

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

fun createCircularProgressDrawable(context: Context) =
    CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
