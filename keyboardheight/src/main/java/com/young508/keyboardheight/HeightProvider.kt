package com.young508.keyboardheight

import android.app.Activity
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.EditText
import android.widget.PopupWindow
import androidx.core.view.*
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HeightProvider(private val mActivity: Activity) : PopupWindow(mActivity),
    OnGlobalLayoutListener, DefaultLifecycleObserver {
    private val rootView: View = View(mActivity)
    private var mImeChangeListener: ImeHeightListener? = null
    private var mImeAnimationListener: ImeAnimationListener? = null

    private var screenHeight = 0
    private var mNavigationHeight = 0
    private var addCall: Boolean = false

    private val isSupportInsetsFiled by lazy {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    private val executeHandle = Handler(Looper.getMainLooper()) {
        if (it.what == 1) {
            showAtLocation(mActivity.window.decorView, Gravity.NO_GRAVITY, 0, 0)
        }
        true
    }

    init {
        // 基础配置
        contentView = rootView
        // 监听全局Layout变化
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        setBackgroundDrawable(ColorDrawable(0))
        // 设置宽度为0，高度为全屏
        width = 0
        height = WindowManager.LayoutParams.MATCH_PARENT
        if (!isSupportInsetsFiled) {
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        }
        inputMethodMode = INPUT_METHOD_NEEDED
    }

    fun setHeightListener(
        imeChangeListener: ImeHeightListener? = null,
        imeAnimationListener: ImeAnimationListener? = null
    ): HeightProvider {
        this.mImeChangeListener = imeChangeListener
        this.mImeAnimationListener = imeAnimationListener
        return this
    }

    // 延迟加载popupWindow，如果不加延迟就会报错
    fun showProvider() {
        val msg = Message()
        msg.what = 1
        executeHandle.sendMessageDelayed(msg, 100)
//        GlobalScope.launch(Dispatchers.Main) {
//            delay(100)
//            showAtLocation(mActivity.window.decorView, Gravity.NO_GRAVITY, 0, 0)
//        }
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        //rect获取可视范围，没有键盘时，bottom = height(去掉导航栏的高度)，有键盘时，bottom = fullHeight - 键盘高度
        rootView.getWindowVisibleDisplayFrame(rect)

        //这种方式可以准确获取到导航栏的高度，存在有值，不存在为0
        val status = ViewCompat.getRootWindowInsets(mActivity.window.decorView)
            ?.getInsets(WindowInsetsCompat.Type.navigationBars())
        mNavigationHeight = status?.bottom ?: 0

        //没有导航栏的高度，也就是刚开始rect.bottom的值
        val rawHeight = screenHeight - mNavigationHeight

        //rect的bottom变化实际上就是因为键盘和导航栏高度变化才会变化，
        var keyboardHeight = rawHeight - rect.bottom

        if (isSupportInsetsFiled) {
            if (addCall) return
            if (null != mImeAnimationListener) {
                val translateDeferringInsetsAnimationCallback =
                    TranslateDeferringInsetsAnimationCallback(
                        view = mActivity.window.decorView,
                        persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                        deferredInsetTypes = WindowInsetsCompat.Type.ime(),
                        dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE,
                        imeChangeListener = mImeAnimationListener
                    )

                ViewCompat.setOnApplyWindowInsetsListener(
                    mActivity.window.decorView,
                    translateDeferringInsetsAnimationCallback
                )
                ViewCompat.setWindowInsetsAnimationCallback(
                    mActivity.window.decorView,
                    translateDeferringInsetsAnimationCallback
                )
            } else {
                val imeCall = object : ImeHeightListener {
                    override fun imeHeightChanged(
                        height: Int
                    ) {
                        mImeChangeListener?.imeHeightChanged(height)
                    }
                }
                val call = WindowInsetsCallback(imeCall)
                ViewCompat.setOnApplyWindowInsetsListener(mActivity.window.decorView, call)
            }
            addCall = true
        } else {
            if (keyboardHeight < 50.toPx(mActivity)) {
                keyboardHeight = 0
            }
            mImeChangeListener?.imeHeightChanged(keyboardHeight)
        }
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        dismiss()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        screenHeight = mActivity.fullScreenHeight()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        screenHeight = mActivity.fullScreenHeight()
    }
}
