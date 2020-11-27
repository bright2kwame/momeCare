package com.momecarefoundation.app.extension

import android.os.Handler
import androidx.viewpager.widget.ViewPager

fun ViewPager.autoScroll(interval: Long) {
    val handler = Handler()
    var scrollPosition = 0
    val runnable = object : Runnable {
        override fun run() {
            val count = adapter?.count ?: 0
            setCurrentItem(scrollPosition++ % count, true)
            handler.postDelayed(this, interval)
        }
    }
    handler.post(runnable)
}
