package com.young508.keyboardheight

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.*

class WindowInsetsCallback(
    private val listener: ImeHeightListener?,
) : OnApplyWindowInsetsListener {
    private var mStandardHeight: Int = 0

    /**
     * 只有inset有变动就会调用
     * 然后向动画回调传递
     *
     */
    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        insets.let {
            val imeShow =
                ViewCompat.getRootWindowInsets(v)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
            val typesInset = insets.getInsets(WindowInsetsCompat.Type.ime())
            val otherInset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val diff = Insets.subtract(typesInset, otherInset).let {
                Insets.max(it, Insets.NONE)
            }
            if (mStandardHeight == 0 && diff.bottom > 50.toPx(v.context)) {
                mStandardHeight = diff.bottom
            }
            listener?.imeHeightChanged(diff.bottom, mStandardHeight, imeShow)
        }
        return WindowInsetsCompat.CONSUMED
    }

}
