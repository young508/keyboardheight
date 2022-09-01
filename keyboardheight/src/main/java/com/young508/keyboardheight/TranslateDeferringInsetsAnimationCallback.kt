package com.young508.keyboardheight

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.*

class TranslateDeferringInsetsAnimationCallback(
    private val view: View,
    val persistentInsetTypes: Int,
    val deferredInsetTypes: Int,
    val imeChangeListener: ImeAnimationListener?,
    var animationRunning: Boolean = false,
    dispatchMode: Int = DISPATCH_MODE_STOP
) :
    WindowInsetsAnimationCompat.Callback(dispatchMode), OnApplyWindowInsetsListener {

    var mStandardHeight: Int = 0

    init {
        require(persistentInsetTypes and deferredInsetTypes == 0) {
            "persistentInsetTypes and deferredInsetTypes can not contain any of " + " same WindowInsetsCompat.Type values"
        }
    }

    override fun onStart(
        animation: WindowInsetsAnimationCompat,
        bounds: WindowInsetsAnimationCompat.BoundsCompat
    ): WindowInsetsAnimationCompat.BoundsCompat {

        val imeShow =
            ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false

        imeChangeListener?.imeChangeStart(imeShow)
        animationRunning = true
        return bounds
    }

    /**
     * 动画出现/结束首先调用该方法
     * 比 [onApplyWindowInsets]还会早一些
     */
    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
        super.onPrepare(animation)
        animationRunning = true

    }

    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnimations: List<WindowInsetsAnimationCompat>
    ): WindowInsetsCompat {
        val typesInset = insets.getInsets(deferredInsetTypes)
        val otherInset = insets.getInsets(persistentInsetTypes)

        val diff = Insets.subtract(typesInset, otherInset).let {
            Insets.max(it, Insets.NONE)
        }

        val imeShow = insets.isVisible(WindowInsetsCompat.Type.ime())

        if (mStandardHeight > 0 && imeShow) {
            imeChangeListener?.imeHeightProgress(imeShow, Math.min(diff.bottom, mStandardHeight))
        } else {
            imeChangeListener?.imeHeightProgress(imeShow, diff.bottom)
        }

        animationRunning = true
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {

        val imeShow =
            ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
        animationRunning = false
        imeChangeListener?.imeChangeEnd(imeShow)

    }

    /**
     * 只有inset有变动就会调用
     * 然后向动画回调传递
     *
     */
    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        insets.let {

            val typesInset = insets.getInsets(deferredInsetTypes)
            val otherInset = insets.getInsets(persistentInsetTypes)

            val diff = Insets.subtract(typesInset, otherInset).let {
                Insets.max(it, Insets.NONE)
            }

            if (mStandardHeight == 0 && diff.bottom > 50.toPx(v.context)) {
                mStandardHeight = diff.bottom
            }

            if (!animationRunning) {
                if (mStandardHeight > 0) {
                    imeChangeListener?.imeButtonChangeHeight(diff.bottom, mStandardHeight)
                }
            }
        }
        return WindowInsetsCompat.CONSUMED
    }

}
