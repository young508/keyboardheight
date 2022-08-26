package com.young508.keyboardheight

import android.app.Activity
import android.content.Context
import androidx.window.layout.WindowMetricsCalculator


fun Activity.fullScreenHeight(): Int {
    val metrics = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(this)
    return metrics.bounds.height()
}

fun Int.toPx(context: Context): Float {
    return this * context.resources.displayMetrics.density
}