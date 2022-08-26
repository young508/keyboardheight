package com.young.keyboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import com.young508.keyboardheight.HeightProvider
import com.young508.keyboardheight.ImeAnimationListener
import com.young508.keyboardheight.ImeHeightListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var standard = 0
        var mCurrentImeShow = false
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        val height1 = HeightProvider(this)
        lifecycle.addObserver(height1)

        height1.setHeightListener(imeChangeListener = object : ImeHeightListener {
            override fun imeHeightChanged(height: Int, standardHeight: Int, imeShow: Boolean) {
                if (!mCurrentImeShow && imeShow && standardHeight > 0) {
                    findViewById<EditText>(R.id.et_1).translationY =
                        -(Math.min(height, standardHeight)).toFloat()
                } else {
                    findViewById<EditText>(R.id.et_1).translationY = -height.toFloat()
                }
                mCurrentImeShow = imeShow
            }
        })

        // 延迟加载popupwindow，如果不加延迟就会报错
        GlobalScope.launch(Dispatchers.Main) {
            delay(100)
            height1.showAtLocation(this@MainActivity.window.decorView, Gravity.NO_GRAVITY, 0, 0)
        }

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        val height2 = HeightProvider(this)
        lifecycle.addObserver(height2)

        height2.setHeightListener(imeAnimationListener = object : ImeAnimationListener {
            override fun imeChangeStart(imeShow: Boolean) {

            }

            override fun imeHeightProgress(imeShow: Boolean, height: Int) {
                if (standard > 0 && imeShow) {
                    findViewById<EditText>(R.id.et_2).translationY =
                        (-(height.coerceAtMost(standard))).toFloat()
                } else {
                    findViewById<EditText>(R.id.et_2).translationY = -height.toFloat()
                }
            }

            override fun imeChangeEnd(imeShow: Boolean) {

            }

            override fun imeButtonChangeHeight(height: Int, standardHeight: Int) {
                standard = standardHeight
                findViewById<EditText>(R.id.et_2).translationY = -height.toFloat()
            }
        })

        // 延迟加载popupwindow，如果不加延迟就会报错
        GlobalScope.launch(Dispatchers.Main) {
            delay(100)
            height2.showAtLocation(this@MainActivity.window.decorView, Gravity.NO_GRAVITY, 0, 0)
        }
    }
}